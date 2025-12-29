package com.skmr.googletrans.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TranslationRequest {
    @Size(max = 10, message = "Source language must be an ISO-639-1 code")
    private String sourceLanguage;

    @NotBlank(message = "Target language is required")
    @Size(max = 10, message = "Target language must be an ISO-639-1 code")
    private String targetLanguage;

    @NotBlank(message = "Text to translate is required")
    private String text;

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
