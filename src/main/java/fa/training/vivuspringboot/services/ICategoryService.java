package fa.training.vivuspringboot.services;

import fa.training.vivuspringboot.dtos.category.CategoryCreateUpdateDTO;
import fa.training.vivuspringboot.dtos.category.CategoryDTO;
import fa.training.vivuspringboot.entities.Category;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {

    /**
     * Find all categories
     * @return list of categories
     */
    List<CategoryDTO> findAll();

    /**
     * Find category by id
     * @param id category id
     * @return category
     */
    CategoryDTO findById(UUID id);

    /**
     * Create category
     * @param categoryCreateUpdateDTO categoryCreateUpdateDTO need to create
     * @return CategoryDTO
     */
    CategoryDTO create(CategoryCreateUpdateDTO categoryCreateUpdateDTO);

    /**
     * Delete category
     * @param id category id
     * @return true if deleted, false if not
     */
    boolean delete(UUID id);

    /**
     * Update category
     * @param id category id
     * @return CategoryDTO
     */
    CategoryDTO update(UUID id, CategoryCreateUpdateDTO categoryCreateUpdateDTO);

    /**
     * Delete category
     * @param id category id
     * @param isSoftDelete true if soft delete, false if hard delete
     * @return true if deleted, false if not
     */
    boolean delete(UUID id, boolean isSoftDelete);
}
