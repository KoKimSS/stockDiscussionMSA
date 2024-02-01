package com.example.stockmsauser.config.jwt;

import com.example.stockmsauser.config.auth.PrincipalDetails;
import com.example.stockmsauser.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {

    public Long getUserId(){
        return findUserFromAuth().getId();
    }

    public static User findUserFromAuth() {
        Authentication authentication = getAuthentication();
        if (!isValidAuthentication(authentication)) return null;
        if (!isInstanceOfPrincipalDetails(authentication)) {
            return null;
        }
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        return user;
    }

    private static boolean isInstanceOfPrincipalDetails(Authentication authentication) {
        return authentication.getPrincipal() instanceof PrincipalDetails;
    }

    private static boolean isValidAuthentication(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
