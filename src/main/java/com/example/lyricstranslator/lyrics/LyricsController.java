package com.example.lyricstranslator.lyrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/lyrics")
public class LyricsController {

    private final LyricsService lyricsService;

    @Autowired
    public LyricsController(LyricsService lyricsService) {
        this.lyricsService = lyricsService;
    }

    @GetMapping("{artist}/{song}")
    public ResponseEntity<String> getLyrics(@PathVariable("artist") String artist, @PathVariable("song") String song) {
        return new ResponseEntity<>(lyricsService.receiveLyrics(artist,song), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> translateLyrics(@RequestBody TranslateRequest request)
            throws IOException, InterruptedException {
        String content = lyricsService.lyricsTranslation(request);
        return new ResponseEntity<>(content,HttpStatus.OK);
    }
}
