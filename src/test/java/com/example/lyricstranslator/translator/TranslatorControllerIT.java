package com.example.lyricstranslator.translator;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TranslatorControllerIT {
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
  void shouldTranslateLyrics() {
    mockServer
        .expect(times(1), requestTo("https://genius.com/Metallica-the-unforgiven-lyrics"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withSuccess(
                "<!DOCTYPE html>\n"
                    + "<html lang=\"en\">\n"
                    + "  <head>\n"
                    + "    <title>Test</title>\n"
                    + "  </head>\n"
                    + "  <body>\n"
                    + "    <div class=\"Lyrics__Container\">\n"
                    + "      <span class=\"ReferentFragment\">First line</span>\n"
                    + "      <span class=\"ReferentFragment\">Second line</span>\n"
                    + "      <span class=\"ReferentFragment\">Third line</span>\n"
                    + "    </div>\n"
                    + "    <div class=\"Lyrics__Container\">\n"
                    + "      <span class=\"ReferentFragment\">4 line</span>\n"
                    + "      <span class=\"ReferentFragment\">5 line</span>\n"
                    + "      <span class=\"ReferentFragment\">6 line</span>\n"
                    + "    </div>\n"
                    + "  </body>\n"
                    + "</html>",
                MediaType.APPLICATION_JSON));

    RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .get("/translator")
        .then()
        .log()
        .all()
        .statusCode(200);
  }
}
