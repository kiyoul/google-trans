package com.skmr.googletrans.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.translate.v3.TranslationServiceSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class TranslationClientConfig {
    @Bean(destroyMethod = "close")
    public TranslationServiceClient translationServiceClient(
            @Value("${google.cloud.credentials-location:#{null}}") Resource credentialsResource) throws IOException {
        TranslationServiceSettings.Builder builder = TranslationServiceSettings.newBuilder();

        if (credentialsResource != null) {
            try (InputStream inputStream = credentialsResource.getInputStream()) {
                ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(inputStream);
                builder.setCredentialsProvider(FixedCredentialsProvider.create(credentials));
            }
        }

        return TranslationServiceClient.create(builder.build());
    }
}
