package com.example.lyricstranslator.translator;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TranslatorService {
    public static String translate(String content,String baseLang, String langTranslate) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://google-translate1.p.rapidapi.com/language/translate/v2"))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("Accept-Encoding","application/gzip")
                .header("X-RapidAPI-Host", "google-translate1.p.rapidapi.com")
                .header("X-RapidAPI-Key", "") //Key
                .method("POST", HttpRequest.BodyPublishers.ofString("source="+baseLang+"&target="+langTranslate
                        +"&q="+content))
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

        return parseTranslationJson(response.body());
    }

    public static String parseTranslationJson(String content){
        JSONObject contentObject = new JSONObject(content);
        JSONObject data = contentObject.getJSONObject("data");
        JSONArray translations = data.getJSONArray("translations");
        JSONObject text = translations.getJSONObject(0);

        return text.getString("translatedText");
    }
}
