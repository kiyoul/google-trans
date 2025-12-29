package com.skmr.googletrans.model;

public class TranslationResponse {
    private final String translatedText;
    private final String detectedSourceLanguage;
    private final String targetLanguage;

    public TranslationResponse(String translatedText, String detectedSourceLanguage, String targetLanguage) {
        this.translatedText = translatedText;
        this.detectedSourceLanguage = detectedSourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public String getDetectedSourceLanguage() {
        return detectedSourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }
}
