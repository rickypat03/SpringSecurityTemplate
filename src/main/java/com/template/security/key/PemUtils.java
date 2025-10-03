package com.template.security.key;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class PemUtils {

    /**
     * Read the key from the param path and cleans it.
     * @param path The path of the key inside resource directory
     * @return The key cleaned
     * @throws Exception If there is some problem with the cleaning
     */
    private String readKey(String path) throws Exception {

        ClassPathResource resource = new ClassPathResource(path);

        try (InputStream is = resource.getInputStream()) {

            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            return key.replace("-----BEGIN PUBLIC KEY -----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY -----", "")
                    .replace("-----END PRIVATE KEY -----", "")
                    .replaceAll("\\s+", "");
        }
    }

    /**
     * Loads an RSA PrivateKey from the private.pem file inside resources/keys/
     *
     * @return the PrivateKey
     * @throws Exception if there is an error during key loading
     */
    public PrivateKey loadPrivateKey() throws Exception {

        String keyPath = "keys/private.pem";
        String keyPEM = readKey(keyPath);

        byte[] decoded = Base64.getDecoder().decode(keyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    /**
     * Loads an RSA PublicKey from the public.pem file inside resources/keys/
     *
     * @return the PublicKey
     * @throws Exception if there is an error during key loading
     */
    public PublicKey loadPublicKey() throws Exception {

        String keyPath = "keys/public.pem";
        String keyPEM = readKey(keyPath);

        byte[] decoded = Base64.getDecoder().decode(keyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
