package de.eblaas.products.datasource;

import static io.reactivex.Flowable.empty;
import static java.util.stream.Collectors.toList;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import de.eblaas.products.datasource.events.DataImportErrorEvent;
import de.eblaas.products.datasource.events.DataImportEvent;
import de.eblaas.products.domain.Product;
import de.eblaas.products.service.ProductService;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * Collects all registered (spring context) data sources, combine, batch and save to database
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DatasourceCollector implements ApplicationListener<ApplicationReadyEvent> {

  private final List<ProductDatasource> dataSources;
  private final ProductService productService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
  private int batchSize = 5;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    subscribeToDatasources();
  }

  @VisibleForTesting
  void subscribeToDatasources() {

    log.info("Registered datasources={}", dataSources.stream().map(ProductDatasource::type).collect(toList()));

    // add error handlers to all streams
    var streamsWithErrorHandling =
        dataSources.stream()
                   .map(ProductDatasource::getProductStream)
                   .map(stream -> stream.doOnError(e -> log.warn("Datasource error={}", e.getMessage()))
                                        .onErrorResumeNext(empty()))
                   .collect(toList());

    // batch stream and save products
    Flowable.merge(streamsWithErrorHandling)
            .buffer(200, TimeUnit.MICROSECONDS, batchSize)
            .filter(products -> !products.isEmpty())
            .subscribeOn(Schedulers.io())
            .subscribe(this::saveProducts);
  }

  private void saveProducts(List<Product> products) {
    try {
      log.debug("Save products\n{}", Joiner.on("\n").join(products));
      productService.saveAll(products);
      applicationEventPublisher.publishEvent(new DataImportEvent(this, products));
    } catch (Exception e) {
      log.warn("Collector error={}", e.getMessage(), e);
      applicationEventPublisher.publishEvent(new DataImportErrorEvent(this, products, e));
    }
  }
}
