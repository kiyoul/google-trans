package com.skmr.googletrans.service;

import com.skmr.googletrans.model.TranslationRequest;
import com.skmr.googletrans.model.TranslationResponse;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TranslationService {
    private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);

    private final TranslationServiceClient translationServiceClient;
    private final String projectId;
    private final String location;

    public TranslationService(TranslationServiceClient translationServiceClient,
                              @Value("${google.cloud.project-id}") String projectId,
                              @Value("${google.cloud.location:global}") String location) {
        this.translationServiceClient = translationServiceClient;
        this.projectId = projectId;
        this.location = location;
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


        return new TranslationResponse(
                translation.getTranslatedText(),
                detectedLanguage,
                request.getTargetLanguage()
        );
    }
}
