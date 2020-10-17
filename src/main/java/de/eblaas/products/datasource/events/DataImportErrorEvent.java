package de.eblaas.products.datasource.events;

import de.eblaas.products.domain.Product;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Application event containing import errors, may be used for error handling
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class DataImportErrorEvent extends ApplicationEvent {

  private final List<Product> products;
  private final Exception error;

  public DataImportErrorEvent(Object source, List<Product> products, Exception error) {
    super(source);
    this.products = products;
    this.error = error;
  }
}
