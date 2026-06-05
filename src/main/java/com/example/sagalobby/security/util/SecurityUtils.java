package com.example.sagalobby.security.util;

import com.example.sagalobby.common.exception.InvalidAuthenticationPrincipalException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;



    @Service
    public class SecurityUtils {

        public UUID getCurrentUser() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if(auth == null || !auth.isAuthenticated()) {
                throw new InvalidAuthenticationPrincipalException("No Authenticated User Found");
            }

            Object principal = auth.getPrincipal();
            if (!(principal instanceof Map<?,?> principalMap)) {
                throw new InvalidAuthenticationPrincipalException("Invalid Principal Type");
            }

            Object personIdObj = principalMap.get("personId");
            if(personIdObj == null) {
                throw new InvalidAuthenticationPrincipalException("Principal Id Does Not Exist");
            }

            UUID personId = UUID.fromString(personIdObj.toString());
            return personId;
        }
    }

