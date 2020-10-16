package de.eblaas.products.configuration;

import de.eblaas.products.controller.mapper.ProductMapper;
import de.eblaas.products.datasource.ProductDatasource;
import de.eblaas.products.datasource.impl.FileImportDatasource;
import de.eblaas.products.datasource.impl.HttpProductDatasource;
import de.eblaas.products.datasource.impl.KafkaDatasource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class DatasourceConfig {

    @Bean
    @Profile("dev")
    public ProductDatasource mockDataFileDatasource() {
        return new FileImportDatasource(new ClassPathResource("mockdata.csv"));
    }

    @Bean
    public ProductDatasource kafkaDatasource() {
        return new KafkaDatasource();
    }

    @Bean
    public ProductDatasource httpDatasource(ProductMapper productMapper) {
        return new HttpProductDatasource(productMapper);
    }
}
