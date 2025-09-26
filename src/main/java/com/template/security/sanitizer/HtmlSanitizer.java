package com.template.security.sanitizer;


import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizer {

    private static final Safelist NONE = Safelist.none();

    public String stripAll(String input) {
        if (input == null) return null;
        return Jsoup.clean(input, NONE);
    }

    //todo if needed in future
    // public String basic(String input) {
    //     if (input == null) return null;
    //     return Jsoup.clean(input, BASIC);
    // }
}
