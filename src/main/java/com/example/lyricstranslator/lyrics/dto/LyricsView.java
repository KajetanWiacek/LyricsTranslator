package com.example.lyricstranslator.lyrics.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LyricsView {
    String language;
    String lyrics;
}
