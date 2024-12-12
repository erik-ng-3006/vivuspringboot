package fa.training.vivuspringboot.services;

import fa.training.vivuspringboot.dtos.category.CategoryCreateUpdateDTO;
import fa.training.vivuspringboot.dtos.category.CategoryDTO;
import fa.training.vivuspringboot.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Searches for categories that match the given keyword and returns a paginated result.
     *
     * @param keyword the keyword to search for in the categories
     * @param pageable the pagination and sorting information
     * @return a paginated list of categories that match the search criteria
     */
    Page<CategoryDTO> searchAll(String keyword, Pageable pageable);

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
