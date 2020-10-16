package de.eblaas.products.configuration;

import de.eblaas.products.domain.Product;
import de.eblaas.products.repository.ProductRepository;
import de.eblaas.products.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartupEvent implements ApplicationListener<ApplicationReadyEvent> {

    private final ProductRepository productRepository;
    private final SearchService searchService;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Product entity = new Product();
        entity.setId(UUID.randomUUID().toString());
        entity.setName("Pro Camera");
        entity.setCategory("multimedia");
        entity.setRating(2);
        productRepository.save(entity);

        log.info("Db elements {}", productRepository.findAll());
        //log.info("Indexed elements {}", searchService.getProductByName("camera", Optional.empty(), PageRequest.of(0, 10)));
    }
}
