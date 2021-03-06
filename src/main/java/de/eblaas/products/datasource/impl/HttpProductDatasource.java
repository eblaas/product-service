package de.eblaas.products.datasource.impl;

import de.eblaas.products.controller.dto.ProductBody;
import de.eblaas.products.controller.dto.ProductMapper;
import de.eblaas.products.datasource.ProductDatasource;
import de.eblaas.products.domain.Product;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Http push data source for putting new products, may be extended by file upload
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/products")
public class HttpProductDatasource implements ProductDatasource {

  private final ProductMapper productMapper;

  private final PublishProcessor<Product> stream = PublishProcessor.create();


  @ResponseStatus(code = HttpStatus.OK)
  @PutMapping
  public void putProduct(@RequestBody @Valid ProductBody body) {
    stream.onNext(productMapper.toDomain(body));
  }

  @Override
  public Flowable<Product> getProductStream() {
    return stream;
  }

  @Override
  public String type() {
    return "HttpPushProductDatasource";
  }
}
