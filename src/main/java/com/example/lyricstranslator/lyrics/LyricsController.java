package com.example.lyricstranslator.lyrics;

import com.example.lyricstranslator.lyrics.dto.LyricsView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lyrics")
@RequiredArgsConstructor
public class LyricsController {
  private final LyricsService lyricsService;

  @GetMapping
  public LyricsView getLyrics(@RequestParam String phrase) {
    return lyricsService.getLyrics(phrase);
  }

  @GetMapping("/translate")
  public LyricsView translateLyrics(@RequestParam String phrase, @RequestParam String targetLang) {
    return lyricsService.translateLyrics(phrase, targetLang);
  }
}
