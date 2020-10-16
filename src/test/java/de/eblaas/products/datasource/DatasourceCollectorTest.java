package de.eblaas.products.datasource;

import de.eblaas.products.domain.Product;
import de.eblaas.products.repository.ProductRepository;
import io.reactivex.Flowable;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import static java.util.Collections.singletonList;
import static org.awaitility.Awaitility.await;

class DatasourceCollectorTest {


    @Test
    void testIfCollectorSavesProductsAndPublishEvents() {

        var product = new Product();
        product.setId("1");
        product.setName("mock");

        var productRepository = Mockito.mock(ProductRepository.class);
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
        }), productRepository, eventPublisher);

        collector.subscribeToDatasources();

        await()
            .atMost(Duration.ONE_SECOND)
            .untilAsserted(() -> {
                Mockito.verify(productRepository).saveAll(singletonList(product));
                Mockito.verify(eventPublisher).publishEvent(new DatasourceDataEvent(collector, singletonList(product)));
            });


    }
}
