package com.example.lyricstranslator.genius;

import com.example.lyricstranslator.lyrics.dto.LyricsView;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class GeniusService {
  private final RestTemplate restTemplate;
  private final LyricsParser lyricsParser;

  @Value("${api.genius-api-url}")
  private String geniusApiUrl;

  @Value("${genius.token}")
  private String geniusToken;

  private static final String SEARCH_EP_URL = "/search";
  private static final String CONTENT_REQUEST_PARAM = "q";
  private static final Integer FIRST_INDEX = 0;

  public LyricsView searchSong(String phrase) {
    SongData songData = searchSongUrl(phrase);

    String responseLyrics =
        restTemplate
            .exchange(songData.getLyricsUrl(), HttpMethod.GET, null, String.class)
            .getBody();

    String lyrics = lyricsParser.parseHtmlLyrics(responseLyrics);

    return LyricsView.builder().language(songData.getLanguage()).lyrics(lyrics).build();
  }

  private SongData searchSongUrl(String phrase) {
    String url =
        UriComponentsBuilder.fromUriString(geniusApiUrl + SEARCH_EP_URL)
            .queryParam(CONTENT_REQUEST_PARAM, phrase)
            .build()
            .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(geniusToken);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    String songResponse =
        restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class).getBody();

    JSONObject songData =
        new JSONObject(songResponse)
            .getJSONObject("response")
            .getJSONArray("hits")
            .getJSONObject(FIRST_INDEX)
            .getJSONObject("result");

    return SongData.builder()
        .language(songData.getString("language"))
        .lyricsUrl(songData.getString("url"))
        .build();
  }
}
