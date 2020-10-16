package de.eblaas.products.datasource;

import de.eblaas.products.domain.Product;
import io.reactivex.Flowable;

public interface ProductDatasource {

    Flowable<Product> getProductStream();

    String type();
}


