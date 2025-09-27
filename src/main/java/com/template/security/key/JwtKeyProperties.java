package com.template.security.key;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class maps the JWT key properties from application configuration.
 * It holds the private and public keys used for JWT signing and verification.
 * You can load the keys from an AWS Parameter Store with an import in application.yml or application.properties.
 * The keys should be in PEM format and have to be named:
 * - jwt.privateKey
 * - jwt.publicKey
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtKeyProperties {
    private String privateKey;
    private String publicKey;
}
