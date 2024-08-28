package com.mydrive.backend.services.utils;

import org.slf4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Utilidad para cifrar y descifrar archivos usando JCA (Java Cryptography Architecture).
 *
 * <p>Esta clase proporciona métodos para cifrar y descifrar archivos con el algoritmo AES (Advanced Encryption Standard)
 * en modo CBC (Cipher Block Chaining) con relleno PKCS5. La cifrado y descifrado se realiza utilizando una clave derivada
 * de una contraseña proporcionada y un salt (sal) generado aleatoriamente. La clave es derivada utilizando el algoritmo PBKDF2
 * con HMAC-SHA256.</p>
 *
 * <p>Los métodos disponibles en esta clase permiten cifrar archivos de entrada y guardarlos en archivos de salida cifrados,
 * así como descifrar archivos cifrados y guardarlos en archivos de salida descifrados.</p>
 *
 * <p>La clase utiliza {@link Logger} para el registro de eventos y excepciones.</p>
 *
 * @see SecretKey
 * @see Cipher
 * @see SecretKeyFactory
 * @see PBEKeySpec
 * @see CipherInputStream
 * @see CipherOutputStream
 */
public class FileEncryptionUtil {

    /**
     * Algoritmo de cifrado utilizado, en este caso AES (Advanced Encryption Standard).
     */
    private static final String ALGORITHM = "AES";

    /**
     * Transformación del cifrador que incluye el algoritmo, el modo de operación (CBC)
     * y el esquema de relleno (PKCS5Padding).
     */
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * Tamaño del salt (sal) en bytes. Se utiliza para añadir aleatoriedad al proceso de derivación de la clave.
     */
    private static final int SALT_SIZE = 16;

    /**
     * Tamaño del vector de inicialización (IV) en bytes. El IV es utilizado en el modo CBC para asegurar
     * que el mismo texto plano, produzca un texto cifrado diferente cada vez que se cifre.
     */
    private static final int IV_SIZE = 16;

    /**
     * Tamaño de la clave en bits. En este caso, se utiliza una clave de 256 bits para el cifrado AES.
     */
    private static final int KEY_SIZE = 256;

    /**
     * Número de iteraciones para la derivación de la clave. Se utiliza en PBKDF2 para incrementar la seguridad.
     */
    private static final int ITERATION_COUNT = 65536;

    /**
     * Deriva una clave secreta a partir de una contraseña y un salt utilizando PBKDF2 con HMAC-SHA256.
     *
     * <p>Este método utiliza la contraseña proporcionada y el salt para generar una clave secreta
     * de 256 bits, que es utilizada para cifrar o descifrar archivos.</p>
     *
     * @param password La contraseña utilizada para derivar la clave secreta.
     * @param salt El salt utilizado en el proceso de derivación de la clave.
     * @return La clave secreta derivada.
     * @throws NoSuchAlgorithmException Si el algoritmo de cifrado especificado no está disponible.
     * @throws InvalidKeySpecException Si el formato de la clave especificado es inválido.
     */
    public static SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException,
            InvalidKeySpecException {

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Cifra un archivo de entrada y guarda el archivo cifrado en el archivo de salida.
     *
     * <p>Este método genera un salt y un vector de inicialización (IV) aleatorios, deriva la clave secreta
     * utilizando la contraseña proporcionada, y luego cifra el archivo de entrada. El salt y el IV se escriben
     * al principio del archivo cifrado para su uso durante el descifrado.</p>
     *
     * @param inputFile El archivo de entrada que se va a cifrar.
     * @param outputFile El archivo de salida donde se guardará el archivo cifrado.
     * @param password La contraseña utilizada para derivar la clave secreta.
     * @throws Exception Si ocurre un error durante el cifrado del archivo.
     */
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

    /**
     * Descifra un archivo cifrado y guarda el archivo descifrado en el archivo de salida.
     *
     * <p>Este método lee el salt y el IV del archivo cifrado, deriva la clave secreta utilizando la contraseña
     * proporcionada, y luego descifra el archivo. El archivo descifrado se guarda en el archivo de salida.</p>
     *
     * @param inputFile El archivo cifrado que se va a descifrar.
     * @param outputFile El archivo de salida donde se guardará el archivo descifrado.
     * @param password La contraseña utilizada para derivar la clave secreta.
     * @throws Exception Si ocurre un error durante el descifrado del archivo o si la contraseña es incorrecta.
     */
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

    /**
     * Descifra un flujo de entrada y escribe los datos descifrados en un flujo de salida.
     *
     * <p>Este método utiliza un {@link CipherInputStream} para leer y descifrar datos del flujo de entrada
     * y luego escribe los datos descifrados en el flujo de salida.</p>
     *
     * @param fis El flujo de entrada del archivo cifrado.
     * @param fos El flujo de salida donde se guardará el archivo descifrado.
     * @param cipher El objeto {@link Cipher} configurado para descifrar.
     * @throws IOException Si ocurre un error al leer o escribir datos.
     * @throws BadPaddingException Si ocurre un error relacionado con el relleno durante el descifrado.
     * @throws IllegalBlockSizeException Si ocurre un error relacionado con el tamaño del bloque durante el descifrado.
     */
    private static void decryptStream(InputStream fis, OutputStream fos, Cipher cipher) throws IOException,
            BadPaddingException, IllegalBlockSizeException {

        try (CipherInputStream cis = new CipherInputStream(fis, cipher)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}
