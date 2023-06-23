package com.example.lyricstranslator.lyrics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/lyrics")
@RequiredArgsConstructor
public class LyricsController {
  private final LyricsService lyricsService;

  @GetMapping("{artist}/{song}")
  public ResponseEntity<String> getLyrics(
      @PathVariable("artist") String artist, @PathVariable("song") String song) {
    return new ResponseEntity<>(lyricsService.receiveLyrics(artist, song), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<String> translateLyrics(@RequestBody TranslateRequest request)
      throws IOException, InterruptedException {
    String content = lyricsService.lyricsTranslation(request);
    return new ResponseEntity<>(content, HttpStatus.OK);
  }
}
