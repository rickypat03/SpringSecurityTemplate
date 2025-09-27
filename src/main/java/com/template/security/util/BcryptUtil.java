package com.template.security.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BcryptUtil {

    /**
     * Bean to create a PasswordEncoder using BCrypt.
     * @return PasswordEncoder that uses BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

}