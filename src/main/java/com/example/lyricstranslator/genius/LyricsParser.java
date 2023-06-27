package com.example.lyricstranslator.genius;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class LyricsParser {
  private static final String LYRICS_CONTAINER_CLASS = "div[class*=\"Lyrics__Container\"]";

  private static final String QUOTATION_MARK = "\"";

  private static final String EMPTY_STRING = "";

  public String parseHtmlLyrics(String html) {
    Document document = Jsoup.parse(html);
    Elements lyricsContainers = document.select(LYRICS_CONTAINER_CLASS);

    return lyricsContainers.stream()
        .map(Element::text)
        .collect(Collectors.joining())
        .replace(QUOTATION_MARK, EMPTY_STRING);
  }
}
