package com.template.security.key;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
@RequiredArgsConstructor
public class JwtKeyConfig {

    private final PemUtils pemUtils;

    @Bean
    public PrivateKey jwtPrivateKey() throws Exception {
        return pemUtils.loadPrivateKey();
    }

    @Bean
    public PublicKey jwtPublicKey() throws Exception {
        return pemUtils.loadPublicKey();
    }
}
