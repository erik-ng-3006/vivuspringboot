package fa.training.vivuspringboot.services;
import java.util.List;
import java.util.UUID;

import fa.training.vivuspringboot.dtos.category.CategoryDTO;
import fa.training.vivuspringboot.dtos.product.ProductCreateUpdateDTO;
import fa.training.vivuspringboot.dtos.product.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    /**
     * Get all Product
     * @return List<ProductDTO>
     */
    List<ProductDTO> findAll();
    /**
     * Get Product by id
     * @param id The id of Product
     * @return ProductDTO by id
     */
    ProductDTO findById(UUID id);

    /**
     * Searches for all products that match the specified keyword and returns a paginated result.
     *
     * @param keyword The keyword to search for in the product list.
     * @param pageable The pageable object defining the pagination and sorting information.
     * @return A page of ProductDTO objects that match the search criteria.
     */
    Page<ProductDTO> searchAll(String keyword, Pageable pageable);

    /**
     * Create Product
     * @param productCreateUpdateDTO ProductCreateUpdateDTO need to create
     * @return ProductDTO created
     */
    ProductDTO create(ProductCreateUpdateDTO productCreateUpdateDTO);
    /**
     * Update Product
     * @param id The id of Product
     * @param productCreateUpdateDTO ProductCreateUpdateDTO need to update
     * @return ProductDTO updated
     */
    ProductDTO update(UUID id, ProductCreateUpdateDTO productCreateUpdateDTO);
    /**
     * Delete Product
     * @param id The id of Product
     * @return True if delete success, otherwise false
     */
    boolean delete(UUID id);
    /**
     * Delete Product
     * @param id The id of Product
     * @param isSoftDelete True if soft delete, otherwise false
     * @return True if delete success, otherwise false
     */
    boolean delete(UUID id, boolean isSoftDelete);
}