package de.eblaas.products.datasource;

import de.eblaas.products.domain.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
public class DatasourceDataEvent extends ApplicationEvent {
    private final List<Product> products;

    public DatasourceDataEvent(Object source, List<Product> products) {
        super(source);
        this.products = products;
    }
}
