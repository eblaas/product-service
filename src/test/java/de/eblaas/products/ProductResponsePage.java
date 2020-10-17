package de.eblaas.products;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import de.eblaas.products.controller.dto.ProductResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ProductResponsePage extends PageImpl<ProductResponse> {

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public ProductResponsePage(@JsonProperty("content") List<ProductResponse> content,
      @JsonProperty("number") int number,
      @JsonProperty("size") int size,
      @JsonProperty("totalElements") Long totalElements,
      @JsonProperty("pageable") JsonNode pageable,
      @JsonProperty("last") boolean last,
      @JsonProperty("totalPages") int totalPages,
      @JsonProperty("sort") JsonNode sort,
      @JsonProperty("first") boolean first,
      @JsonProperty("numberOfElements") int numberOfElements) {
    super(content, PageRequest.of(number, size), totalElements);
  }

  public ProductResponsePage(List<ProductResponse> content, Pageable pageable, long total) {
    super(content, pageable, total);
  }

  public ProductResponsePage(List<ProductResponse> content) {
    super(content);
  }

  public ProductResponsePage() {
    super(new ArrayList<ProductResponse>());
  }

}
