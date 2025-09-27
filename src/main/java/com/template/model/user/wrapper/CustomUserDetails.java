package com.template.model.user.wrapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    //TODO: Add only the fields that you want to expose in the JWT token

    private final Long userId;
    private final String username;
    private final String email;
    private final String role;


    private final List<GrantedAuthority> authorities;

    /**
     * Constructs a CustomUserDetails from JWT claims.
     * @param userId The user ID.
     * @param username The username.
     * @param email The user's email.
     * @param role The user's role (e.g., ROLE_USER).
     */
    public CustomUserDetails(Long userId,
                             String username,
                             String email,
                             String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;

        this.authorities = List.of(new SimpleGrantedAuthority(role));
    }

    /**
     * Factory method to conveniently create from claims.
     */
    public static CustomUserDetails fromClaims(Long userId,
                                               String username,
                                               String role) {
        return new CustomUserDetails(userId, username, null, role);
    }

    /**
     * Get the password (not used in JWT stateless).
     * @return The password, or null if not applicable.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * The authorities granted to the user.
     * @return The collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Get the password (not used in JWT stateless).
     * @return The password, or empty string if not applicable.
     */
    @Override
    public String getPassword() {
        return "";
    }


    /**
     * Check if the account is enabled.
     * @return true if the account is enabled, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Check if the account is not locked.
     * @return true if the account is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Check if the credentials are not expired.
     * @return true if the credentials are not expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}

