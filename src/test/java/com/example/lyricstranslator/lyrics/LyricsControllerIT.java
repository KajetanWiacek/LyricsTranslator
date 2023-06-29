package com.example.lyricstranslator.lyrics;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LyricsControllerIT {
  @LocalServerPort private Integer port;
  @Autowired RestTemplate restTemplate;
  private MockRestServiceServer mockServer;

  @BeforeEach
  void setup() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.port = port;
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  void shouldGetLyrics() {
    // given
    String searchResponse = loadFile("src/test/resources/lyrics/search.json");
    String expectedSearchUrl = "https://api.genius.com/search?q=never%20gonna%20give%20you%20up";
    String expectedLyricsUrl = "https://test/never-gonna-give-you-up";
    String lyricsResponse = loadFile("src/test/resources/lyrics/test.html");

    mockServer
        .expect(times(1), requestTo(expectedSearchUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(searchResponse, MediaType.APPLICATION_JSON));

    mockServer
        .expect(times(1), requestTo(expectedLyricsUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(lyricsResponse, MediaType.APPLICATION_JSON));

    // when // then
    RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .queryParam("phrase", "never gonna give you up")
        .get("/lyrics")
        .then()
        .log()
        .all()
        .statusCode(200);
  }

  @Test
  void shouldTranslateLyrics() {
    // given
    String searchResponse = loadFile("src/test/resources/lyrics/search.json");
    String expectedSearchUrl = "https://api.genius.com/search?q=never%20gonna%20give%20you%20up";
    String expectedLyricsUrl = "https://test/never-gonna-give-you-up";
    String lyricsResponse = loadFile("src/test/resources/lyrics/test.html");
    String translateCommand = loadFile("src/test/resources/lyrics/translate_command.json");
    String translateResponse = loadFile("src/test/resources/lyrics/translate_response.json");

    mockServer
        .expect(times(1), requestTo(expectedSearchUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(searchResponse, MediaType.APPLICATION_JSON));

    mockServer
        .expect(times(1), requestTo(expectedLyricsUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(lyricsResponse, MediaType.APPLICATION_JSON));

    mockServer
        .expect(times(1), requestTo("/test/translate"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().string(translateCommand))
        .andRespond(withSuccess(translateResponse, MediaType.APPLICATION_JSON));

    // when // then
    RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .queryParam("phrase", "never gonna give you up")
        .queryParam("targetLang", "pl")
        .get("/lyrics/translate")
        .then()
        .log()
        .all()
        .statusCode(200);
  }

  @SneakyThrows
  private String loadFile(String path) {
    return Files.readString(Path.of(path));
  }
}
