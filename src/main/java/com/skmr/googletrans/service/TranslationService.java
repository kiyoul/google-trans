package com.skmr.googletrans.service;

import com.google.cloud.translate.v3.GetSupportedLanguagesRequest;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.SupportedLanguage;
import com.google.cloud.translate.v3.SupportedLanguages;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.skmr.googletrans.model.SupportedLanguageInfo;
import com.skmr.googletrans.model.TranslationRequest;
import com.skmr.googletrans.model.TranslationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TranslationService {
    private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);

    private final TranslationServiceClient translationServiceClient;
    private final String projectId;
    private final String location;

    public TranslationService(TranslationServiceClient translationServiceClient,
                              @Value("${google.cloud.project-id:}") String projectId,
                              @Value("${google.cloud.location:global}") String location) {
        this.translationServiceClient = translationServiceClient;
        this.projectId = resolveProjectId(projectId);
        this.location = location;
    }

    private String resolveProjectId(String configuredProjectId) {
        if (StringUtils.hasText(configuredProjectId)) {
            return configuredProjectId;
        }

        String[] envKeys = {"GOOGLE_CLOUD_PROJECT", "GCLOUD_PROJECT", "GOOGLE_CLOUD_PROJECT_ID", "GCP_PROJECT"};
        for (String key : envKeys) {
            String value = System.getenv(key);
            if (StringUtils.hasText(value)) {
                logger.info("Using Google Cloud project id from environment variable {}", key);
                return value;
            }
        }

        throw new IllegalArgumentException("google.cloud.project-id not configured and no GOOGLE_CLOUD_PROJECT-related env var found");
    }

    public TranslationResponse translate(TranslationRequest request) {
        logger.info("Translating text. targetLanguage={}, text={}", request.getTargetLanguage(), request.getText());
        String parent = LocationName.of(projectId, location).toString();
        TranslateTextRequest.Builder builder = TranslateTextRequest.newBuilder()
                .setParent(parent)
                .setTargetLanguageCode(request.getTargetLanguage())
                .addContents(request.getText())
                .setMimeType("text/plain");

        if (StringUtils.hasText(request.getSourceLanguage())) {
            builder.setSourceLanguageCode(request.getSourceLanguage());
        }

        TranslateTextResponse response = translationServiceClient.translateText(builder.build());
        Translation translation = response.getTranslations(0);
        String detectedLanguage = translation.getDetectedLanguageCode();
        if (!StringUtils.hasText(detectedLanguage)) {
            detectedLanguage = request.getSourceLanguage();
        }

        logger.info("translation.getDetectedLanguageCode() :{}", translation.getDetectedLanguageCode());
        return new TranslationResponse(
                translation.getTranslatedText(),
                detectedLanguage,
                request.getTargetLanguage()
        );
    }

    public List<SupportedLanguageInfo> listSupportedLanguages(String displayLanguage) {
        String parent = LocationName.of(projectId, location).toString();
        GetSupportedLanguagesRequest.Builder requestBuilder = GetSupportedLanguagesRequest.newBuilder()
                .setParent(parent);

        if (StringUtils.hasText(displayLanguage)) {
            requestBuilder.setDisplayLanguageCode(displayLanguage);
        }

        SupportedLanguages supportedLanguages = translationServiceClient.getSupportedLanguages(requestBuilder.build());
        return supportedLanguages.getLanguagesList()
                .stream()
                .map(language -> new SupportedLanguageInfo(language.getLanguageCode(), language.getDisplayName()))
                .collect(Collectors.toList());
    }
}
