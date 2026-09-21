package com.lycheetech.movingapi.config;
import com.google.cloud.firestore.pipeline.stages.Offset;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware",dateTimeProviderRef= "offsetDateTimeProvider")
public class JpaAuditingConfig {
    @Bean
    public DateTimeProvider offsetDateTimeProvider(){
        return () -> Optional.of(OffsetDateTime.now());
    }
}
