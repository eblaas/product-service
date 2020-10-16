package de.eblaas.products.service;

import de.eblaas.products.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public Page<Product> getProductByName(String name, Optional<String> category, Pageable page) {

        var fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        var queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

        Query nameQuery = queryBuilder.keyword().wildcard().onField("name").matching(name.toLowerCase()).createQuery();

        var finalQuery = queryBuilder.bool().must(nameQuery);
        if (category.isPresent()) {
            finalQuery.must(queryBuilder.keyword().onField("category").matching(category.get().toLowerCase()).createQuery());
        }

        var fullTextQuery = fullTextEntityManager.createFullTextQuery(finalQuery.createQuery(), Product.class);
        fullTextQuery.setSort(queryBuilder.sort().byField("rating").desc().createSort());
        fullTextQuery.setMaxResults(page.getPageSize());
        fullTextQuery.setFirstResult((int) page.getOffset());

        return new PageImpl<Product>(fullTextQuery.getResultList(), page, fullTextQuery.getResultSize());
    }


}
