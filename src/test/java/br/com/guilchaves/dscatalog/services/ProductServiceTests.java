package br.com.guilchaves.dscatalog.services;

import br.com.guilchaves.dscatalog.dto.ProductDTO;
import br.com.guilchaves.dscatalog.entities.Category;
import br.com.guilchaves.dscatalog.entities.Product;
import br.com.guilchaves.dscatalog.projections.ProductProjection;
import br.com.guilchaves.dscatalog.repositories.CategoryRepository;
import br.com.guilchaves.dscatalog.repositories.ProductRepository;
import br.com.guilchaves.dscatalog.services.exceptions.DatabaseException;
import br.com.guilchaves.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.guilchaves.dscatalog.utils.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Category category;
    private Product product;
    private ProductDTO productDTO;
    private ProductProjection productProjection;
    private PageImpl<Product> page;
    private PageImpl<ProductProjection> productPage;


    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        category = Factory.createCategory();
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        productProjection = Factory.createProductProjection(1L, "Product");
        page = new PageImpl<>(List.of(product));
        productPage = new PageImpl<>(List.of(productProjection, productProjection, productProjection));

        when(repository.findAll((Pageable) any())).thenReturn(page);

        when(repository.searchProducts(any(), any(), (Pageable) any())).thenReturn((PageImpl) productPage);
        when(repository.searchProductsWithCategories(anyList())).thenReturn(List.of(product));

        when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        when(repository.findById(existingId)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(repository.getReferenceById(existingId)).thenReturn(product);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        doNothing().when(repository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        when(repository.existsById(existingId)).thenReturn(true);
        when(repository.existsById(nonExistingId)).thenReturn(false);
        when(repository.existsById(dependentId)).thenReturn(true);
    }

    @Test
    public void findAllShouldReturnPage() {
        String productName = "";
        String categoryId = "0";
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAll(productName, categoryId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(3, result.getSize());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO dto = service.findById(existingId);

        Assertions.assertNotNull(dto);
        verify(repository, times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.findById(nonExistingId));
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO dto = service.update(existingId, productDTO);
        Assertions.assertNotNull(dto);
    }

    @Test
    public void updateShouldReturnResourceNotFoundWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.update(nonExistingId, productDTO)
        );
    }


    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> service.delete(existingId));
        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.delete(nonExistingId)
        );
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class,
                () -> service.delete(dependentId)
        );
    }
}
