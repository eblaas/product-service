package de.eblaas.products.service;

import de.eblaas.products.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

interface ProductRepository extends PagingAndSortingRepository<Product, String> {

  Page<Product> findByName(String name, Pageable pageable);

  Page<Product> findByNameAndCategory(String name, String category, Pageable pageable);
}
