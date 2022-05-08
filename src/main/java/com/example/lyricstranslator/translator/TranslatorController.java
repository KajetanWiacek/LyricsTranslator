package com.example.lyricstranslator.translator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/translator")
public class TranslatorController {
    @PostMapping("{baseLang}/{trLang}")
    public ResponseEntity<String> translateLyrics(@PathVariable("baseLang") String baseLang,
           @PathVariable("trLang") String trLang, @RequestBody String content)
            throws IOException, InterruptedException {
        String translated = TranslatorService.translate(content,baseLang,trLang);
        return new ResponseEntity<>(translated, HttpStatus.OK);
    }
}
