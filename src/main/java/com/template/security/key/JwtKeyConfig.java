package com.template.security.key;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class JwtKeyConfig {

    private final PemUtils pemUtils;

    public JwtKeyConfig(PemUtils pemUtils) {
        this.pemUtils = pemUtils;
    }

    @Bean
    public PrivateKey jwtPrivateKey() throws Exception {
        return pemUtils.loadPrivateKey();
    }

    @Bean
    public PublicKey jwtPublicKey() throws Exception {
        return pemUtils.loadPublicKey();
    }
}
