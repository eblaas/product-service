package de.eblaas.products.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductBody {
    private String id;
    private String name;
    private String category;
}
