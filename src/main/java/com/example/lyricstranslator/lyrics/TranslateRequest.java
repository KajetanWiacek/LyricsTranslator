package com.example.lyricstranslator.lyrics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TranslateRequest {
    private String artist;
    private String song;
    private String baseLang;
    private String trLang;
}
