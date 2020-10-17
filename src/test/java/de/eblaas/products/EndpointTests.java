package de.eblaas.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.FIVE_SECONDS;

import de.eblaas.products.controller.dto.ProductBody;
import de.eblaas.products.domain.Category;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.jpa.properties.hibernate.search.default.indexBase=data/test"})
class EndpointTests {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testSearchEndpoint() {

    await().atMost(FIVE_SECONDS).untilAsserted(
        () -> assertThat(searchProduct("camera", Category.multimedia)).hasSize(2));
  }

  @Test
  public void datasourceEndpointTest() {

    ProductBody data = new ProductBody("123", "testproduct", Category.other, "https://mock");

    this.restTemplate.put(getServerUriBuilder().toUriString(), data);

    await().atMost(FIVE_SECONDS).untilAsserted(
        () -> assertThat(searchProduct(data.getName(), data.getCategory()).getContent()).hasOnlyOneElementSatisfying(
            product -> assertThat(product.getId()).isEqualTo(data.getId())));
  }

  private UriComponentsBuilder getServerUriBuilder() {
    return UriComponentsBuilder.fromUriString("http://localhost:" + port).path("api/v1/products");
  }

  private ProductResponsePage searchProduct(String name, Category category) {
    URI searchUrl = getServerUriBuilder().queryParam("name", name)
                                         .queryParam("category", category.toString())
                                         .build()
                                         .encode()
                                         .toUri();

    return this.restTemplate.getForObject(searchUrl, ProductResponsePage.class);
  }
}
