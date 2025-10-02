package com.template.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class StringUtils {

    /**
     * Normalizes a string by stripping leading and trailing whitespace
     * and converting it to lowercase using the ROOT locale.
     * @param input the string to normalize
     * @return the normalized string
     */
    public String normalizeString(String input) {
        return input.strip().toLowerCase(Locale.ROOT);
    }

    /**
     * Normalizes a role string by trimming whitespace, converting to uppercase,
     * and ensuring it starts with "ROLE_" as the Spring Security convention.
     * @param raw the raw role string
     * @return the normalized role string
     */
    public String normalizeRole(String raw) {

        String r = raw.trim().toUpperCase();
        return r.startsWith("ROLE_") ? r : "ROLE_" + r;
    }
}
