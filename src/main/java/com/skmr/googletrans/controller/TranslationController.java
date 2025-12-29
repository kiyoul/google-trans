package com.skmr.googletrans.controller;

import com.skmr.googletrans.model.TranslationRequest;
import com.skmr.googletrans.model.TranslationResponse;
import com.skmr.googletrans.service.TranslationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {
    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }

    @PostMapping
    public TranslationResponse translate(@Valid @RequestBody TranslationRequest request) {
        return translationService.translate(request);
    }
}
