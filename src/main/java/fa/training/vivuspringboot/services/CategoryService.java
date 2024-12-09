package fa.training.vivuspringboot.services;

import fa.training.vivuspringboot.dtos.category.CategoryCreateUpdateDTO;
import fa.training.vivuspringboot.dtos.category.CategoryDTO;
import fa.training.vivuspringboot.entities.Category;
import fa.training.vivuspringboot.repositories.ICategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> new CategoryDTO(category.getId(), category.getName(), category.getDescription())).toList();
    }

    @Override
    public CategoryDTO findById(UUID id) {
        Category category = categoryRepository.findById(id).orElse(null);

        //convert to dto
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        return categoryDTO;
    }

    @Override
    public CategoryDTO create(CategoryCreateUpdateDTO categoryCreateUpdateDTO) {
        if (categoryCreateUpdateDTO == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (categoryRepository.findByName(categoryCreateUpdateDTO.getName()) != null) {
            throw new IllegalArgumentException("Category name already exists");
        }
        //Convert to category
        Category category = new Category();
        category.setName(categoryCreateUpdateDTO.getName());
        category.setDescription(categoryCreateUpdateDTO.getDescription());
        category.setActive(true);

        //Save to database
        Category newCategory = categoryRepository.save(category);

        //Convert to dto
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(newCategory.getId());
        categoryDTO.setName(newCategory.getName());
        categoryDTO.setDescription(newCategory.getDescription());

        //return dto
        return categoryDTO;
    }

    @Override
    public boolean delete(UUID id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }
        //Set deletedAt
        categoryRepository.delete(category);

        return !categoryRepository.existsById(id);
    }

    @Override
    public boolean delete(UUID id, boolean isSoftDelete) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }
        if (isSoftDelete) {
            category.setDeletedAt(ZonedDateTime.now());
            categoryRepository.save(category);
            return true;
        }
        return delete(id);
    }

    @Override
    public CategoryDTO update(UUID id, CategoryCreateUpdateDTO categoryCreateUpdateDTO) {
        // Check null object
        if (categoryCreateUpdateDTO == null) {
            throw new IllegalArgumentException("Category is null");
        }

        // Check if category not existing
        var existingCategory = categoryRepository.findById(id).orElse(null);

        if (existingCategory == null) {
            throw new IllegalArgumentException("Category is not existed");
        }

        // Check unique name if not the same id
        var existingCategorySameName = categoryRepository.findByName(categoryCreateUpdateDTO.getName());

        if (existingCategorySameName != null && !existingCategorySameName.getId().equals(id)) {
            throw new IllegalArgumentException("Category name is existed");
        }

        // Convert to category to update
        existingCategory.setName(categoryCreateUpdateDTO.getName());
        existingCategory.setDescription(categoryCreateUpdateDTO.getDescription());
        existingCategory.setActive(categoryCreateUpdateDTO.isActive());
        existingCategory.setUpdatedAt(ZonedDateTime.now());

        // Save to database
        var updatedCategory = categoryRepository.save(existingCategory);
        // Return true if success, otherwise false

        // Convert to DTO
        var categoryDTO = new CategoryDTO();
        categoryDTO.setId(updatedCategory.getId());
        categoryDTO.setName(updatedCategory.getName());
        categoryDTO.setDescription(updatedCategory.getDescription());

        // Return dto
        return categoryDTO;
    }
}
