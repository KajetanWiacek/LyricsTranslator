package com.example.lyricstranslator.translator.dto;

import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TranslateView {
  String translatedText;
  LanguageView detectedLanguage;
}
