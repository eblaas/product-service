package de.eblaas.products.datasource.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import de.eblaas.products.datasource.ProductDatasource;
import de.eblaas.products.domain.Category;
import de.eblaas.products.domain.Product;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import java.io.BufferedReader;
import java.io.FileReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

/**
 * Reads a csv file [id,name,category,storeUrl] and publishes to product stream.
 */
@Slf4j
@RequiredArgsConstructor
public class FileImportDatasource implements ProductDatasource {

  private final Resource resource;

  private Flowable<String> loadFileContent(Resource resource) {

    return Flowable.using(
        () -> new BufferedReader(new FileReader(resource.getFile())),
        reader -> Flowable.fromIterable(() -> reader.lines().iterator()),
        BufferedReader::close
    );
  }

  private Flowable<Product> parseProduct(String[] parts) {
    try {
      return Flowable.just(
          Product.builder()
                 .id(checkNotNull(parts[0], "product id must not be null"))
                 .name(checkNotNull(parts[1], "product name must not be null"))
                 .category(toCategory(checkNotNull(parts[2], "product category must not be null")))
                 .seller(checkNotNull(parts[3], "seller must not be null"))
                 .storeUrl(checkNotNull(parts[4], "store url must not be null"))
                 .rating(0)
                 .build());
    } catch (Exception e) {
      log.warn("Invalid csv entry. error={}, data={}", e.getMessage(), parts);
      return Flowable.empty();
    }
  }

  private Category toCategory(String category) {
    try {
      return Category.valueOf(category);
    } catch (IllegalArgumentException e) {
      return Category.other;
    }
  }


  @Override
  public Flowable<Product> getProductStream() {
    return Flowable.just(resource)
                   .observeOn(Schedulers.io())
                   .flatMap(this::loadFileContent)
                   .map(line -> line.split(";"))
                   .flatMap(this::parseProduct);
  }

  @Override
  public String type() {
    return "FileBatchDatasource";
  }
}
