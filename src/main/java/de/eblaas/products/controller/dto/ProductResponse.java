package de.eblaas.products.controller.dto;

import de.eblaas.products.domain.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductResponse {

  private String id;
  private String name;
  private Category category;
  private int rating;
  private String storeUrl;
}
