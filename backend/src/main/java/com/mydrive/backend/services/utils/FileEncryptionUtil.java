package com.mydrive.backend.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FileEncryptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileEncryptionUtil.class);

    public static InputStream decryptFile(InputStream encryptedInputStream, SecretKey secretKey) throws Exception {
        // Configura el cifrador para descifrar
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Descifra el archivo
        CipherInputStream cipherInputStream = new CipherInputStream(encryptedInputStream, cipher);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        cipherInputStream.close();
        encryptedInputStream.close();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public static InputStream encryptFile(InputStream inputStream, SecretKey secretKey) throws Exception {
        // Configura el cifrador para cifrar
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Cifra el archivo
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            cipherOutputStream.write(buffer, 0, bytesRead);
        }

        cipherOutputStream.flush();
        cipherOutputStream.close();
        inputStream.close();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
