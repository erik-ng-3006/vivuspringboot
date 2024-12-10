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

/**
 * Service class for managing categories. This class provides implementations
 * for creating, updating, finding, and deleting categories.
 * It acts as the business layer between the controller and the repository.
 *
 * Responsibilities:
 * - Fetching all categories
 * - Fetching a specific category by its id
 * - Creating a new category
 * - Updating an existing category
 * - Deleting a category (soft delete or hard delete)
 *
 * The class is transactional, ensuring that all operations performed within
 * a transaction scope either succeed collectively or fail collectively.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    /**
     * Retrieves all categories and converts them into a list of CategoryDTO objects.
     *
     * @return a list of CategoryDTO objects containing the id, name, and description
     *         of each category retrieved from the database.
     */
    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> new CategoryDTO(category.getId(), category.getName(), category.getDescription())).toList();
    }


    /**
     * Finds a category by its unique identifier and converts it to a CategoryDTO.
     *
     * @param id the UUID of the category to be retrieved
     * @return the CategoryDTO object if found, or null if no category exists with the given UUID
     */
    @Override
    public CategoryDTO findById(UUID id) {
        return categoryRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        return categoryDTO;
    }

    /**
     * Creates a new category based on the provided CategoryCreateUpdateDTO object.
     *
     * @param categoryCreateUpdateDTO the data transfer object containing the details of the category to be created
     * @return a CategoryDTO object representing the newly created category
     * @throws IllegalArgumentException if the provided CategoryCreateUpdateDTO is null or if the category name already exists
     */
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

    /**
     * Deletes the category with the specified ID.
     *
     * @param id the unique identifier of the category to be deleted
     * @return true if the category was successfully deleted, false otherwise
     * @throws IllegalArgumentException if the category with the specified ID is not found
     */
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

    /**
     * Deletes a category identified by its unique ID. The deletion can be a
     * soft delete or a hard delete based on the provided isSoftDelete parameter.
     *
     * @param id the unique identifier of the category to be deleted
     * @param isSoftDelete if true, performs a soft delete by marking the category
     *                     as deleted; if false, performs a hard delete
     * @return true if the deletion is successful, whether soft or hard
     * @throws IllegalArgumentException if the category with the specified ID
     *                                  is not found
     */
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

    /**
     * Updates the details of an existing category based on the provided data.
     * Validates the inputs and ensures the category exists and the name is unique if updated.
     *
     * @param id the unique identifier of the category to update
     * @param categoryCreateUpdateDTO the data transfer object containing the updated category details
     * @return a CategoryDTO object containing the updated category details
     * @throws IllegalArgumentException if the provided categoryCreateUpdateDTO is null
     *                                  or if the category to update does not exist
     *                                  or if the category name already exists for another category
     */
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
