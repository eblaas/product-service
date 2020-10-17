package de.eblaas.products.datasource.events;

import de.eblaas.products.domain.Product;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Application event containing new imported Products, used by the {@link de.eblaas.products.service.RatingService}
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class DataImportEvent extends ApplicationEvent {

  private final List<Product> products;

  public DataImportEvent(Object source, List<Product> products) {
    super(source);
    this.products = products;
  }
}
