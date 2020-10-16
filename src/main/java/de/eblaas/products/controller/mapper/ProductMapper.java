package de.eblaas.products.controller.mapper;

import de.eblaas.products.controller.dto.ProductBody;
import de.eblaas.products.controller.dto.ProductResponse;
import de.eblaas.products.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductResponse toResponse(Product product);

    Product toDomain(ProductBody product);
}
