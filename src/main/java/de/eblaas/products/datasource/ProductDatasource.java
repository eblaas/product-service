package de.eblaas.products.datasource;

import de.eblaas.products.domain.Product;
import io.reactivex.Flowable;

/**
 * Interface for a custom datasource.
 */
public interface ProductDatasource {

  /**
   * @return stream of {@code Product}s
   */
  Flowable<Product> getProductStream();

  /**
   * @return Readable datasource name.
   */
  String type();
}


