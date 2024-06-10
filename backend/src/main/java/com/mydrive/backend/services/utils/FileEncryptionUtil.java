package com.mydrive.backend.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class FileEncryptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileEncryptionUtil.class);

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int SALT_SIZE = 16;
    private static final int IV_SIZE = 16;
    private static final int KEY_SIZE = 256;
    private static final int ITERATION_COUNT = 65536;

    public static SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static void encryptFile(File inputFile, File outputFile, String password) throws Exception {
        // Generate salt and IV
        byte[] salt = new byte[SALT_SIZE];
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        random.nextBytes(iv);

        // Derive key
        SecretKey secretKey = deriveKey(password, salt);

        // Configure the cipher
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        // Write salt and IV to the output file
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher);
             FileInputStream fis = new FileInputStream(inputFile)) {

            fos.write(salt);
            fos.write(iv);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void decryptFile(File inputFile, File outputFile, String password) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            // Leer el salt del archivo cifrado
            byte[] salt = new byte[SALT_SIZE];
            if (fis.read(salt) != SALT_SIZE) {
                throw new IOException("Error reading salt from file");
            }

            // Leer el IV del archivo cifrado
            byte[] iv = new byte[IV_SIZE];
            if (fis.read(iv) != IV_SIZE) {
                throw new IOException("Error reading IV from file");
            }

            // Derivar la clave usando la contraseña y el salt
            SecretKey secretKey = deriveKey(password, salt);

            // Configurar el descifrador
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            // Intentar descifrar el archivo
            try {
                decryptStream(fis, fos, cipher);
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                throw new Exception("Incorrect password or corrupted file", e);
            }
        }
    }

    private static void decryptStream(InputStream fis, OutputStream fos, Cipher cipher) throws IOException, BadPaddingException, IllegalBlockSizeException {
        try (CipherInputStream cis = new CipherInputStream(fis, cipher)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}
