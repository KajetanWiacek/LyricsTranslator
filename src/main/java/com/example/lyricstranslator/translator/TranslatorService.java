package com.example.lyricstranslator.translator;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslatorService {

  private final RestTemplate restTemplate;

  public String translate(String content, String baseLang, String langTranslate) {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create("https://google-translate1.p.rapidapi.com/language/translate/v2"))
            .header("content-type", "application/x-www-form-urlencoded")
            .header("Accept-Encoding", "application/gzip")
            .header("X-RapidAPI-Host", "google-translate1.p.rapidapi.com")
            .header("X-RapidAPI-Key", "") // Key
            .method(
                "POST",
                HttpRequest.BodyPublishers.ofString(
                    "source=" + baseLang + "&target=" + langTranslate + "&q=" + content))
            .build();

    HttpResponse<String> response = null;
    try {
      response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return parseTranslationJson(response.body());
  }

  public void test() {
    var a =
        restTemplate
            .exchange(
                "https://genius.com/Metallica-the-unforgiven-lyrics",
                HttpMethod.GET,
                null,
                String.class)
            .getBody();
    Document document = Jsoup.parse(a);
    List<String> dupa = new ArrayList<>();
    Elements element = document.select("div[class*=\"Lyrics__Container\"]");
    element.forEach(
        b -> b.select("span[class*=\"ReferentFragment\"]").forEach(c -> dupa.add(c.text())));
    var b = a;
  }

  private String parseTranslationJson(String content) {
    JSONObject contentObject = new JSONObject(content);
    JSONObject data = contentObject.getJSONObject("data");
    JSONArray translations = data.getJSONArray("translations");
    JSONObject text = translations.getJSONObject(0);

    return text.getString("translatedText");
  }
}
