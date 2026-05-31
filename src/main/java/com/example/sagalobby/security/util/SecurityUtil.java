package com.example.sagalobby.security.util;

import com.example.sagalobby.common.exception.InvalidAuthenticationPrincipalException;
import com.example.sagalobby.domain.postgres.playerprofile.PlayerProfile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

public class SecurityUtil {

    @Service
    public class SecurityUtils {

        public PlayerProfile getCurrentUser() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();

            if (!(principal instanceof PlayerProfile creator)) {
                throw new InvalidAuthenticationPrincipalException("Invalid authentication principal");
            }
            return creator;
        }
    }
}
