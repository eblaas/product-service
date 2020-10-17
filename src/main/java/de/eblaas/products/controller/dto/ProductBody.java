package de.eblaas.products.controller.dto;

import de.eblaas.products.domain.Category;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class ProductBody {

  @NotBlank
  private String id;
  @NotBlank
  private String name;
  @NotNull
  private Category category;
  @NotBlank
  private String storeUrl;
}
