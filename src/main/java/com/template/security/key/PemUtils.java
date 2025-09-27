package com.template.security.key;

import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class PemUtils {

    private final JwtKeyProperties jwtKeyProperties;

    public PemUtils(JwtKeyProperties jwtKeyProperties) {
        this.jwtKeyProperties = jwtKeyProperties;
    }

    /**
     * Loads an RSA PrivateKey from a PEM formatted string.
     *
     * @return the PrivateKey
     * @throws Exception if there is an error during key loading
     */
    public PrivateKey loadPrivateKey() throws Exception {
        String cleaned = jwtKeyProperties.getPrivateKey()
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(cleaned);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    /**
     * Loads an RSA PublicKey from a PEM formatted string.
     *
     * @return the PublicKey
     * @throws Exception if there is an error during key loading
     */
    public PublicKey loadPublicKey() throws Exception {
        String cleaned = jwtKeyProperties.getPublicKey()
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(cleaned);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
