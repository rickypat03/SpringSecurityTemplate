package com.template.dto;

import com.template.security.sanitizer.annotation.NoXSS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    //TODO: Add other fields as needed

    @NoXSS
    private String username;

    @NoXSS
    private String email;

    @NoXSS
    private String role;
}
