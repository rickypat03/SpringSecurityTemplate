package com.template.security.util;

import com.template.dto.UserDTO;
import com.template.model.user.wrapper.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility to access current user data from SecurityContext.
 */
public final class UserUtils {


    private UserUtils() {
    }


    /**
     * Used to get the current Authentication from context.
     * @return the current Authentication, or null if not authenticated.
     */
    public static Authentication getAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Used to check if there is an authenticated (non-anonymous) user.
     * @return true if there is an authenticated user.
     */
    public static boolean isAuthenticated() {

        Authentication auth = getAuthentication();

        return auth != null
                && auth.isAuthenticated()
                && !(auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser"));
    }

    /**
     * Extracts the CustomUserDetails (the wrapper around your User entity) from the context.
     * @return the CustomUserDetails or null if not authenticated or if the principal is not
     */
    public static CustomUserDetails getCustomUserDetails() {

        Authentication auth = getAuthentication();

        if (auth == null || !isAuthenticated()) return null;

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails) return (CustomUserDetails) principal;

        return null;
    }

    /**
     * Extracts the UserDTO (or equivalent model) of the current user.
     * @return UserDTO or null if not authenticated
     */
    public static UserDTO getCurrentUser() {

        CustomUserDetails cud = getCustomUserDetails();

        return (cud != null ? new UserDTO(
                cud.getEmail(),
                cud.getUsername(),
                cud.getRole()
        ) : null);
    }

    /**
     * ID of the current user, or null if not authenticated.
     */
    public static Long getCurrentUserId() {

        CustomUserDetails cud = getCustomUserDetails();

        return (cud != null ? cud.getUserId() : null);
    }

    /**
     * Username of the current user, or null if not authenticated.
     */
    public static String getCurrentUsername() {

        CustomUserDetails cud = getCustomUserDetails();

        return (cud != null ? cud.getUsername() : null);
    }

    /**
     * Role of the current user, or null if not authenticated.
     */
    public static String getCurrentUserRole() {

        CustomUserDetails cud = getCustomUserDetails();

        return (cud != null ? cud.getRole() : null);
    }

    /**
     * Checks if the current user has the specified role.
     * @param role the role to check (with or without "ROLE_" prefix)
     * @return true if the user has the role, false otherwise
     */
    public static boolean hasRole(String role) {

        var cud = getCustomUserDetails();

        String normalizedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        return cud != null && cud.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(normalizedRole));
    }
}

