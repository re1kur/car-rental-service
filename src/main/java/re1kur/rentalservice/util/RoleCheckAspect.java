package re1kur.rentalservice.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import re1kur.rentalservice.core.annotation.RequiresRole;

import java.util.Arrays;

@Aspect
@Component
public class RoleCheckAspect {

    @Around("@annotation(requiresRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole)
            throws Throwable {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Authentication required");
        }

        boolean hasRole = Arrays.stream(requiresRole.value())
                .anyMatch(role -> auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_" + role)));

        if (!hasRole) {
            throw new AccessDeniedException("Insufficient permissions");
        }

        return joinPoint.proceed();
    }
}
