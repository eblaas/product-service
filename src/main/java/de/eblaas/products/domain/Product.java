package de.eblaas.products.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@ToString
@Indexed(index = "idx_product")
public class Product {
    @Id
    private String id;

    @Field(store = Store.YES)
    private String name;

    @Field(store = Store.YES)
    private String category;

    @Field(store = Store.YES)
    @SortableField
    private int rating;

}
