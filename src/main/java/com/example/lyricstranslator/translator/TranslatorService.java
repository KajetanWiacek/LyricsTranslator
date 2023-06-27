package com.example.lyricstranslator.translator;

import com.example.lyricstranslator.translator.dto.TranslateCommand;
import com.example.lyricstranslator.translator.dto.TranslateView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TranslatorService {

  private final RestTemplate restTemplate;

  @Value("${api.translator-url}")
  private String translatorUrl;

  private static final String TRANSLATE_EP_URL = "/translate";
  private static final String TRANSLATE_FORMAT = "text";
  private static final String AUTO_LANGUAGE = "text";

  public Optional<TranslateView> translate(String content, String lang) {
    String uri = translatorUrl + TRANSLATE_EP_URL;

    TranslateCommand translateCommand =
        TranslateCommand.builder()
            .content(content)
            .format(TRANSLATE_FORMAT)
            .source(AUTO_LANGUAGE)
            .target(lang)
            .build();

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<TranslateCommand> entity = new HttpEntity<>(translateCommand, headers);

    TranslateView translateView =
        restTemplate.exchange(uri, HttpMethod.GET, entity, TranslateView.class).getBody();

    return Optional.ofNullable(translateView);
  }
}
