package de.eblaas.products;

import de.eblaas.products.controller.dto.ProductBody;
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
class EndpointTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSearchEndpoint() {

        await()
            .atMost(Duration.FIVE_SECONDS)
            .untilAsserted(() -> assertThat(query("camera", "multimedia")).hasSize(2));

    }

    @Test
    public void pushEndpointTest() {
        ProductBody data = new ProductBody("123", "testproduct", "test");

        this.restTemplate.put(getServerUriBuilder().toUriString(), data);
        await()
            .atMost(Duration.FIVE_SECONDS)
            .untilAsserted(() -> assertThat(query(data.getName(), data.getCategory())).hasSize(1));

    }

    private UriComponentsBuilder getServerUriBuilder() {
        return UriComponentsBuilder.fromUriString("http://localhost:" + port).path("api/v1/products");
    }

    private RestResponsePage query(String name, String category) {
        URI searchUrl = getServerUriBuilder()
            .queryParam("name", name)
            .queryParam("category", category)
            .build()
            .encode()
            .toUri();

        return this.restTemplate.getForObject(searchUrl, RestResponsePage.class);
    }
}
