package de.eblaas.products.repository;

import de.eblaas.products.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
}
