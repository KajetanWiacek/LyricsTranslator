package com.example.lyricstranslator.lyrics;

import com.example.lyricstranslator.translator.TranslatorService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class LyricsService {

    public String receiveLyrics(String artist, String song){
        String url = "https://api.lyrics.ovh/v1/"+artist+"/"+song;
        String fixedUrl = url.replace(" ","%20");
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fixedUrl))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().
            send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JSONObject lyricsObj = new JSONObject(response.body());
        String lyrics = lyricsObj.getString("lyrics");

        return lyrics.replace("\n"," ").replace("\r"," ");
    }

    public String lyricsTranslation(TranslateRequest request){
        String lyrics = receiveLyrics(request.getArtist(),request.getSong());

        return TranslatorService.translate(lyrics,request.getBaseLang(),request.getTrLang());
    }
}
