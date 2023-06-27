package com.example.lyricstranslator.translator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TranslateCommand {

  @JsonProperty("q")
  String content;

  String source;

  String target;

  String format;
}
