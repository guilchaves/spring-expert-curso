package br.com.guilchaves.dscatalog.services;

import br.com.guilchaves.dscatalog.dto.ProductDTO;
import br.com.guilchaves.dscatalog.repositories.ProductRepository;
import br.com.guilchaves.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private String productName, categoryId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        productName = "The Lord of the Rings";
        categoryId = "2";
    }

    @Test
    public void findAllShouldReturnPageWhenPage0Size10() {
        productName = "";
        categoryId = "0";
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAll(productName, categoryId, pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllShouldReturnEmptyPageWhenPageDoesNotExists() {
        PageRequest pageRequest = PageRequest.of(50, 10);
        Page<ProductDTO> result = service.findAll(productName, categoryId, pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllShouldReturnOrderedPageWhenSortedByName() {
        productName = "";
        categoryId = "0";

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDTO> result = service.findAll(productName, categoryId, pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(existingId);
        Assertions.assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.delete(nonExistingId)
        );
    }

}
