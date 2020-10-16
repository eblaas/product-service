package de.eblaas.products;

import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.jpa.properties.hibernate.search.default.indexBase=data/test"})
class ProductServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {

        URI searchUrl = UriComponentsBuilder.fromUriString("http://localhost:" + port)
            .path("api/v1/search")
            .queryParam("name", "camera")
            .queryParam("category", "multimedia")
            .build()
            .encode()
            .toUri();

        await()
            .atMost(Duration.TEN_SECONDS)
            .untilAsserted(() -> assertThat(this.restTemplate.getForObject(searchUrl, RestResponsePage.class)).hasSize(1));

    }

}
