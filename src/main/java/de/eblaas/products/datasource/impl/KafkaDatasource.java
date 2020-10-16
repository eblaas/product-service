package de.eblaas.products.datasource.impl;

import de.eblaas.products.datasource.ProductDatasource;
import de.eblaas.products.domain.Product;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class KafkaDatasource implements ProductDatasource {

    @Override
    public Flowable<Product> getProductStream() {
        return Flowable.error(new UnsupportedOperationException("Kafka datasource not implemented."));
    }

    @Override
    public String type() {
        return "KafkaPushDatasource";
    }
}
