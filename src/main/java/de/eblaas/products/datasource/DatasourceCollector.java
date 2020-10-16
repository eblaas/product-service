package de.eblaas.products.datasource;

import com.google.common.annotations.VisibleForTesting;
import de.eblaas.products.repository.ProductRepository;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.reactivex.Flowable.empty;
import static java.util.stream.Collectors.toList;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatasourceCollector implements ApplicationListener<ApplicationReadyEvent> {
    private final List<ProductDatasource> dataSources;
    private final ProductRepository productRepository;
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
        var flowsWithErrorHandling = dataSources.stream()
            .map(ProductDatasource::getProductStream)
            .map(f -> f.doOnError(e -> log.warn("Datasource error={}", e.getMessage(), e)).onErrorResumeNext(empty()))
            .collect(toList());

        //batch stream and save it to database
        Flowable.merge(flowsWithErrorHandling)
            .buffer(200, TimeUnit.MICROSECONDS, batchSize)
            .filter(products -> !products.isEmpty())
            .subscribeOn(Schedulers.io())
            .subscribe(products -> {
                try {
                    log.debug("Save products={}", products);
                    productRepository.saveAll(products);
                    applicationEventPublisher.publishEvent(new DatasourceDataEvent(this, products));
                } catch (Exception e) {
                    log.warn("Collector error={}", e.getMessage(), e);
                }
            });

    }
}
