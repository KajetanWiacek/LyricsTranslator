package com.example.lyricstranslator.translator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/translator")
@RequiredArgsConstructor
public class TranslatorController {
  private final TranslatorService translatorService;

  @PostMapping("{baseLang}/{trLang}")
  public ResponseEntity<String> translateLyrics(
      @PathVariable("baseLang") String baseLang,
      @PathVariable("trLang") String trLang,
      @RequestBody String content) {
    String translated = translatorService.translate(content, baseLang, trLang);
    return new ResponseEntity<>(translated, HttpStatus.OK);
  }

  @GetMapping
  public void dupa() {
    translatorService.test();
  }
}
