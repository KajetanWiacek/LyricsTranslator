package com.example.lyricstranslator.lyrics;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LyricsControllerIT {
  @LocalServerPort private Integer port;
  @Autowired RestTemplate restTemplate;
//  private MockRestServiceServer mockServer;

  @BeforeEach
  void setup() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.port = port;
//    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  void shouldTranslateLyrics() {
    // given
    String lyricsResponse = loadFile("src/test/resources/lyrics/test.html");

    //    mockServer
    //        .expect(times(1), requestTo("https://genius.com/Metallica-the-unforgiven-lyrics"))
    //        .andExpect(method(HttpMethod.GET))
    //        .andRespond(withSuccess(lyricsResponse, MediaType.APPLICATION_JSON));

    // when // then
    RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .queryParam("phrase", "the unforgiven metallica")
        .get("/lyrics")
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
