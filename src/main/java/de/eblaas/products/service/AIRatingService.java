package de.eblaas.products.service;

import de.eblaas.products.datasource.DatasourceDataEvent;
import de.eblaas.products.domain.Product;
import de.eblaas.products.repository.ProductRepository;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class AIRatingService implements ApplicationListener<DatasourceDataEvent> {

    private final ProductRepository productRepository;
    private final Random random = new Random();

    @Override
    public void onApplicationEvent(DatasourceDataEvent event) {
        Flowable.fromIterable(event.getProducts())
            .observeOn(Schedulers.io())
            .flatMap(this::getRanting)
            .buffer(event.getProducts().size())
            .doOnNext(products -> log.debug("Rating updates. data={}", products))
            .subscribe(productRepository::saveAll);
    }

    private Flowable<Product> getRanting(Product product) {
        product.setRating(random.nextInt(6));

        return Flowable.just(product).delay(100, TimeUnit.MILLISECONDS);
    }

}
