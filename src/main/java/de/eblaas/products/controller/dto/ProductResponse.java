package de.eblaas.products.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private String id;
    private String name;
    private String category;
    private int rating;
}
