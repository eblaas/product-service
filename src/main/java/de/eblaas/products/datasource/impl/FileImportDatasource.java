package de.eblaas.products.datasource.impl;

import de.eblaas.products.datasource.ProductDatasource;
import de.eblaas.products.domain.Product;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@RequiredArgsConstructor
public class FileImportDatasource implements ProductDatasource {

    private final Resource resource;

    private Flowable<String> loadFileContent(Resource resource) {

        return Flowable.using(
            () -> new BufferedReader(new FileReader(resource.getFile())),
            reader -> Flowable.fromIterable(() -> reader.lines().iterator()),
            BufferedReader::close
        );
    }

    private Flowable<Product> parseProduct(String[] parts) {
        try {
            var product = new Product();
            product.setId(checkNotNull(parts[0], "product id must not be null"));
            product.setName(checkNotNull(parts[1], "product name must not be null"));
            product.setCategory(checkNotNull(parts[2], "product category must not be null"));
            product.setRating(0);
            return Flowable.just(product);
        } catch (Exception e) {
            return Flowable.error(new IllegalAccessException(
                String.format("Invalid csv entry. error=%s, data=%s", e.getMessage(), parts)));
        }
    }


    @Override
    public Flowable<Product> getProductStream() {
        return Flowable.just(resource)
            .observeOn(Schedulers.io())
            .flatMap(this::loadFileContent)
            .map(line -> line.split(";"))
            .flatMap(this::parseProduct);
    }

    @Override
    public String type() {
        return "FileBatchDatasource";
    }
}
