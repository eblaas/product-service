package de.eblaas.products.service;

import com.google.common.base.Joiner;
import de.eblaas.products.datasource.events.DataImportEvent;
import de.eblaas.products.domain.Product;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * The rating service listens on {@link DataImportEvent} events and populates the rating field. The sample
 * ratingFunction generates a random rating value [0..5] simulating a delay of 100 ms.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RatingService implements ApplicationListener<DataImportEvent> {

  private final ProductService productService;

  private final Function<Product, Flowable<Integer>> ratingFunction = MOCK_RATING_FUNCTION_WITH_DELAY;

  private static Function<Product, Flowable<Integer>> MOCK_RATING_FUNCTION_WITH_DELAY =
      product -> Flowable.just(new Random().nextInt(6))
                         .delay(100, TimeUnit.MILLISECONDS);


  @Override
  public void onApplicationEvent(DataImportEvent event) {
    Flowable.fromIterable(event.getProducts())
            .observeOn(Schedulers.io())
            .flatMap(product -> ratingFunction.apply(product).map(rating -> product.toBuilder().rating(rating).build()))
            .buffer(event.getProducts().size())
            .doOnNext(products -> log.debug("Rating updates.\n{}", Joiner.on("\n").join(products)))
            .subscribe(productService::saveAll);
  }
}
