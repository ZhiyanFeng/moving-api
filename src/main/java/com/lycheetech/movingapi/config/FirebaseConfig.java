package com.lycheetech.movingapi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Initializes the Firebase Admin SDK so incoming Firebase ID tokens
 * (issued by the frontend after Firebase Authentication) can be verified.
 * <p>
 * Credentials are resolved in this order:
 * 1. Path given by {@code firebase.credentials.path} (env FIREBASE_CREDENTIALS_PATH).
 *    Accepts a plain filesystem path or a {@code classpath:} resource path.
 * 2. A {@code google-service-account.json} file on the classpath (src/main/resources)
 * 3. Application Default Credentials (e.g. GOOGLE_APPLICATION_CREDENTIALS env var,
 *    or the default service account when running on GCP)
 */
@Slf4j
@Configuration
public class FirebaseConfig {

    private static final String DEFAULT_CLASSPATH_RESOURCE = "google-service-account.json";

    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(resolveCredentials())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }

    private GoogleCredentials resolveCredentials() throws IOException {
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            log.info("Loading Firebase credentials from: {}", credentialsPath);
            Resource resource = resourceLoader.getResource(credentialsPath);
            if (resource.exists()) {
                try (InputStream in = resource.getInputStream()) {
                    return GoogleCredentials.fromStream(in);
                }
            }
            log.warn("Configured firebase.credentials.path '{}' was not found, trying other options", credentialsPath);
        }

        ClassPathResource resource = new ClassPathResource(DEFAULT_CLASSPATH_RESOURCE);
        if (resource.exists()) {
            log.info("Loading Firebase credentials from classpath resource {}", DEFAULT_CLASSPATH_RESOURCE);
            try (InputStream in = resource.getInputStream()) {
                return GoogleCredentials.fromStream(in);
            }
        }

        log.info("No explicit Firebase credentials configured, falling back to Application Default Credentials");
        return GoogleCredentials.getApplicationDefault();
    }
}

