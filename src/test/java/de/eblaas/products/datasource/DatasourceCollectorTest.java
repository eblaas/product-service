package de.eblaas.products.datasource;

import static java.util.Collections.singletonList;
import static org.awaitility.Awaitility.await;

import de.eblaas.products.datasource.events.DataImportEvent;
import de.eblaas.products.domain.Category;
import de.eblaas.products.domain.Product;
import de.eblaas.products.service.ProductService;
import io.reactivex.Flowable;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

class DatasourceCollectorTest {


  @Test
  void testIfCollectorSavesProductsAndPublishEvents() {

    var product = new Product("1", "mock product", Category.other, 2, "mock", "https://mock");

    var productService = Mockito.mock(ProductService.class);
    var eventPublisher = Mockito.mock(ApplicationEventPublisher.class);

    var collector = new DatasourceCollector(singletonList(new ProductDatasource() {
      @Override
      public Flowable<Product> getProductStream() {
        return Flowable.just(product);
      }

      @Override
      public String type() {
        return "mock";
      }
    }), productService, eventPublisher);

    collector.subscribeToDatasources();

    await()
        .atMost(Duration.ONE_SECOND)
        .untilAsserted(() -> {
          Mockito.verify(productService).saveAll(singletonList(product));
          Mockito.verify(eventPublisher).publishEvent(new DataImportEvent(collector, singletonList(product)));
        });


  }
}
