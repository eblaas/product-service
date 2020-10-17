package de.eblaas.products.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Indexed(index = "idx_product")
public class Product {

  public static final String FIELD_NAME = "name";
  public static final String FIELD_CATEGORY = "category";
  public static final String FIELD_RATING = "rating";
  public static final String FIELD_STORE_URL = "store_url";

  @Id
  @NotBlank
  private String id;

  @NotBlank
  @Field(store = Store.YES, name = FIELD_NAME)
  private String name;

  @NotNull
  @Enumerated(value = EnumType.STRING)
  @Field(store = Store.YES, name = FIELD_CATEGORY)
  private Category category;

  @Field(store = Store.YES, name = FIELD_RATING)
  @SortableField
  private int rating;

  @NotBlank
  @Field(store = Store.YES, index = Index.NO, name = FIELD_STORE_URL)
  private String storeUrl;
}
