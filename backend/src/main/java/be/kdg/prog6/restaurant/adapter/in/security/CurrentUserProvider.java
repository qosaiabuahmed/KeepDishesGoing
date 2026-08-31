package be.kdg.prog6.restaurant.adapter.in.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {


    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be authenticated");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String subject = jwt.getSubject();
            if (subject == null || subject.isEmpty()) {
                throw new IllegalStateException("No subject found in JWT token");
            }
            return subject;
        }

        String username = authentication.getName();
        if (username == null || username.isEmpty()) {
            throw new IllegalStateException("No username found in authentication");
        }

        return username;
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("email");
        }

        return null;
    }
}