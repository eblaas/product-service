package de.eblaas.products.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.eblaas.products.datasource.events.DataImportEvent;
import de.eblaas.products.domain.Category;
import de.eblaas.products.domain.Product;
import java.util.List;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class RatingServiceTest {

  @Test
  void testIfRatingServiceUpdatesProducts() {

    var products = List.of(new Product("1", "mock", Category.tv, -1, "https://mock"));

    var productService = Mockito.mock(ProductService.class);
    var ratingService = new RatingService(productService);

    ratingService.onApplicationEvent(new DataImportEvent(this, products));

    ArgumentCaptor<List<Product>> productSaved = ArgumentCaptor.forClass(List.class);

    await()
        .atMost(Duration.ONE_SECOND)
        .untilAsserted(() -> {
          Mockito.verify(productService).saveAll(productSaved.capture());
          assertThat(productSaved.getValue()).hasOnlyOneElementSatisfying(product ->
              assertThat(product.getRating()).isGreaterThan(0).isLessThan(6));
        });

  }
}
