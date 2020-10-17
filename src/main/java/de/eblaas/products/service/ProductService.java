package de.eblaas.products.service;

import de.eblaas.products.domain.Category;
import de.eblaas.products.domain.Product;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.jpa.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Product service implementation using Hibernate search for providing full text search on product name, filtering on
 * category (optional) sorted by product ranking.
 * <p>
 * 'dev' profile uses in-memory lucene index.
 * <p>
 * 'prod' profile uses elasticsearch for service scalability (several instances of product service)
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductService {

  private final EntityManager entityManager;
  private final ProductRepository productRepository;

  @Transactional(readOnly = true)
  public Page<Product> findProductsByNameAndCategory(String name, Optional<Category> category, Pageable page) {

    var fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    var queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

    var nameQuery = queryBuilder.simpleQueryString().onField(Product.FIELD_NAME).matching(name).createQuery();

    var finalQuery = queryBuilder.bool().must(nameQuery);
    if (category.isPresent()) {
      finalQuery.must(queryBuilder.keyword().onField(Product.FIELD_CATEGORY).matching(category.get()).createQuery());
    }

    var fullTextQuery = fullTextEntityManager.createFullTextQuery(finalQuery.createQuery(), Product.class);
    fullTextQuery.setSort(queryBuilder.sort().byField(Product.FIELD_RATING).desc().createSort());
    fullTextQuery.setMaxResults(page.getPageSize());
    fullTextQuery.setFirstResult((int) page.getOffset());

    return new PageImpl<Product>(fullTextQuery.getResultList(), page, fullTextQuery.getResultSize());
  }

  @Transactional
  public Iterable<Product> saveAll(Iterable<Product> entities) {
    return productRepository.saveAll(entities);
  }

}
