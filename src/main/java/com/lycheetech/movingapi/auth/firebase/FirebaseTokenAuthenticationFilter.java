package com.lycheetech.movingapi.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Verifies the Firebase ID token sent by the frontend on every request
 * (Authorization: Bearer <idToken>) and, if valid, populates the Spring
 * Security context with a {@link FirebaseUserPrincipal}.
 * <p>
 * Invalid/expired/missing tokens simply result in no authentication being set;
 * whether that request is then rejected depends on the authorization rules
 * configured in {@code SecurityConfig} (e.g. public endpoints vs authenticated()).
 */
@Slf4j
@RequiredArgsConstructor
public class FirebaseTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final FirebaseAuth firebaseAuth;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            String idToken = header.substring(BEARER_PREFIX.length());
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);

                FirebaseUserPrincipal principal = new FirebaseUserPrincipal(
                        decodedToken.getUid(),
                        decodedToken.getEmail(),
                        decodedToken.isEmailVerified());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        principal, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                authentication.setDetails(decodedToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (FirebaseAuthException ex) {
                log.warn("Rejected request with invalid Firebase ID token: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}

