package com.template.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
