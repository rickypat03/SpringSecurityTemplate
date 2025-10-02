package com.template.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    //TODO: Add more fields as needed

    @Id
    private Long id;

    private String username;

    private String email;

    private String passwordHash;

    private String role; // e.g., ROLE_USER, ROLE_ADMIN
}
