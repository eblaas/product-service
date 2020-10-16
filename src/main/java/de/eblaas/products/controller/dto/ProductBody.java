package de.eblaas.products.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductBody {
    private String id;
    private String name;
    private String category;
}
