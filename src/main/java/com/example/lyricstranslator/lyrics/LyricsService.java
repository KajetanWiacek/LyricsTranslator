package com.example.lyricstranslator.lyrics;

import com.example.lyricstranslator.translator.TranslatorService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LyricsService {
    private final RestTemplate restTemplate;
    private final List<HttpMessageConverter<?>> messageConverters;

    private final MappingJackson2HttpMessageConverter converter;

    public LyricsService() {
        this.restTemplate = new RestTemplate();
        this.messageConverters = new ArrayList<>();
        this.converter = new MappingJackson2HttpMessageConverter();
    }

    public String receiveLyrics(String artist, String song){
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        Lyrics lyrics = restTemplate.getForObject("https://api.lyrics.ovh/v1/"+artist+"/"+song,Lyrics.class);

        return lyrics.getLyrics().replace("\n"," ");
    }

    public String lyricsTranslation(TranslateRequest request) throws IOException, InterruptedException {
        String lyrics = receiveLyrics(request.getArtist(),request.getSong());

        return TranslatorService.translate(lyrics,request.getBaseLang(),request.getTrLang());
    }
}
