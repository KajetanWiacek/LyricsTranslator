package com.example.lyricstranslator.genius;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SongData {
  String lyricsUrl;
  String language;
}
