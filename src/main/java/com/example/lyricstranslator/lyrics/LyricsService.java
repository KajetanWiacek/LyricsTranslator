package com.example.lyricstranslator.lyrics;

import com.example.lyricstranslator.genius.GeniusService;
import com.example.lyricstranslator.lyrics.dto.LyricsView;
import com.example.lyricstranslator.translator.TranslatorService;
import com.example.lyricstranslator.translator.dto.TranslateView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LyricsService {
  private final GeniusService geniusService;
  private final TranslatorService translatorService;

  public LyricsView getLyrics(String phrase) {
    return geniusService.searchSong(phrase);
  }

  public LyricsView translateLyrics(String phrase, String lang) {
    LyricsView lyrics = geniusService.searchSong(phrase);

    return translatorService
        .translate(lyrics.getLyrics(), lang)
        .map(this::mapToLyricsView)
        .orElse(lyrics);
  }

  private LyricsView mapToLyricsView(TranslateView translateView) {
    return LyricsView.builder()
        .language(translateView.getDetectedLanguage().getLanguage())
        .lyrics(translateView.getTranslatedText())
        .build();
  }
}
