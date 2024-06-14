package net.savantly.encryption.jpa;

import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import jakarta.persistence.AttributeConverter;

// Usure how to use this with the current JPA implementation, because it's not detected in the persistence context
// We can use it manually for now
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AES = "AES";

    private final Key key;
    private final Cipher cipher;

    public AttributeEncryptor(final String secret) throws Exception {
        var bytes = secret.getBytes();
        // make sure the secret is either 128 or 256 bits long
        if (bytes.length != 16 && bytes.length != 32) {
            throw new IllegalArgumentException("Secret must be either 128 or 256 bits long");
        }
        key = new SecretKeySpec(secret.getBytes(), AES);
        cipher = Cipher.getInstance(AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        }
    }
}