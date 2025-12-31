package com.skmr.googletrans.model;

public class SupportedLanguageInfo {
    private final String languageCode;
    private final String displayName;

    public SupportedLanguageInfo(String languageCode, String displayName) {
        this.languageCode = languageCode;
        this.displayName = displayName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getDisplayName() {
        return displayName;
    }
}
