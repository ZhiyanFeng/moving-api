package com.lycheetech.movingapi.auth.firebase;

import lombok.Getter;

/**
 * Represents the authenticated caller, derived from a verified Firebase ID token.
 * Available in controllers/services via {@code SecurityContextHolder} or
 * {@code @AuthenticationPrincipal FirebaseUserPrincipal principal}.
 */
@Getter
public class FirebaseUserPrincipal {

    private final String uid;
    private final String email;
    private final boolean emailVerified;

    public FirebaseUserPrincipal(String uid, String email, boolean emailVerified) {
        this.uid = uid;
        this.email = email;
        this.emailVerified = emailVerified;
    }
}

