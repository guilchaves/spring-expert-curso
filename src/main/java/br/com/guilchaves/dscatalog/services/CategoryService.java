package br.com.guilchaves.dscatalog.services;

import br.com.guilchaves.dscatalog.dto.CategoryDTO;
import br.com.guilchaves.dscatalog.entities.Category;
import br.com.guilchaves.dscatalog.repositories.CategoryRepository;
import br.com.guilchaves.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return new CategoryDTO(category);
    }

}
