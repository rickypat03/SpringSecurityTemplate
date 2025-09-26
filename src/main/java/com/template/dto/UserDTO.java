package com.template.dto;

import com.template.security.sanitizer.annotation.NoHtml;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NoHtml
    private String username;

    @NoHtml
    private String email;

    @NoHtml
    private String role;
}
