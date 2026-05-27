package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.CategoryRequestDTO;
import br.com.fiap.fintech.model.Category;
import br.com.fiap.fintech.model.User;
import br.com.fiap.fintech.repository.CategoryRepository;
import br.com.fiap.fintech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Category> findAll(UUID userId) {
        return categoryRepository.findAllByUserId(userId);
    }

    public Optional<Category> findById(UUID id, UUID userId) {
        return categoryRepository.findByIdAndUserId(id, userId);
    }

    public Category save(CategoryRequestDTO categoryDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Category category = new Category(categoryDto.getName(), categoryDto.getType(), user);
        return categoryRepository.save(category);
    }

    public Category update(UUID id, CategoryRequestDTO categoryDto, UUID userId) {
        return categoryRepository.findByIdAndUserId(id, userId).map(category -> {
            category.setName(categoryDto.getName());
            category.setType(categoryDto.getType());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found or access denied"));
    }

    public void delete(UUID id, UUID userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Category not found or access denied"));
        categoryRepository.delete(category);
    }
}
