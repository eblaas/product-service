package de.eblaas.products.controller;

import de.eblaas.products.controller.dto.ProductMapper;
import de.eblaas.products.controller.dto.ProductResponse;
import de.eblaas.products.domain.Category;
import de.eblaas.products.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/products")
class SearchController {

  private final ProductService productService;
  private final ProductMapper productMapper;

  @GetMapping
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "the page number (0..N)"),
      @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "number of records per page"),
  })
  public Page<ProductResponse> searchProducts(
      @ApiParam(value = "product name", defaultValue = "iphone", required = true) @RequestParam String name,
      @ApiParam(value = "product category") @RequestParam(required = false) Category category,
      @PageableDefault Pageable page) {
    return productService.findProductsByNameAndCategory(name, Optional.ofNullable(category), page)
                         .map(productMapper::toResponse);
  }
}
