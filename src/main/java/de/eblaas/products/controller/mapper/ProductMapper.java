package de.eblaas.products.controller.mapper;

import de.eblaas.products.controller.dto.ProductResponse;
import de.eblaas.products.domain.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(Product product);
}
