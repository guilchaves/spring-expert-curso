package br.com.guilchaves.dscatalog.utils;

import br.com.guilchaves.dscatalog.dto.ProductDTO;
import br.com.guilchaves.dscatalog.entities.Category;
import br.com.guilchaves.dscatalog.entities.Product;
import br.com.guilchaves.dscatalog.projections.ProductProjection;

import java.time.Instant;

public class Factory implements ProductProjection {

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory(){
        return new Category(1L, "Eletronics");
    }

        public static ProductProjection createProductProjection(long l, String product) {
        return new ProductProjection() {
            @Override
            public Long getId() {
                return 1L;
            }
            @Override
            public String getName() {
                return "Phone";
            }

        };
    }

}
