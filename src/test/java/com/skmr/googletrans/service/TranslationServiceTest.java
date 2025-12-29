package com.skmr.googletrans.service;

import com.skmr.googletrans.model.TranslationRequest;
import com.skmr.googletrans.model.TranslationResponse;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

    @Mock
    private TranslationServiceClient translationServiceClient;

    private TranslationService translationService;

    @BeforeEach
    void setUp() {
        translationService = new TranslationService(translationServiceClient, "test-project", "global");
    }

    @Test
    void translateReturnsTranslatedText() {
        TranslateTextResponse response = TranslateTextResponse.newBuilder()
                .addTranslations(Translation.newBuilder()
                        .setTranslatedText("안녕하세요")
                        .setDetectedLanguageCode("en")
                        .build())
                .build();

        when(translationServiceClient.translateText(ArgumentMatchers.any(TranslateTextRequest.class)))
                .thenReturn(response);

        TranslationRequest request = new TranslationRequest();
        request.setTargetLanguage("ko");
        request.setText("hello");

        TranslationResponse result = translationService.translate(request);

        assertThat(result.getTranslatedText()).isEqualTo("안녕하세요");
        assertThat(result.getDetectedSourceLanguage()).isEqualTo("en");
        assertThat(result.getTargetLanguage()).isEqualTo("ko");
    }
}
