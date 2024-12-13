package fa.training.vivuspringboot.controllers;

import fa.training.vivuspringboot.dtos.category.CategoryDTO;
import fa.training.vivuspringboot.dtos.product.ProductCreateUpdateDTO;
import fa.training.vivuspringboot.dtos.product.ProductDTO;
import fa.training.vivuspringboot.services.ICategoryService;
import fa.training.vivuspringboot.services.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/manager/product")
public class ProductController {
    @Value("${spring.data.web.pageable.pageLimit}")
    private Integer pageLimit;

    private final IProductService productService;
    private final ICategoryService categoryService;

    public ProductController(IProductService productService, ICategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    /**
     * Handles GET requests to display a paginated list of products with search capabilities.
     *
     * @param keyword The search keyword to filter the product list (default is an empty string meaning no filter).
     * @param page The current page number for pagination (default is 0, representing the first page).
     * @param size The number of items to display per page (default is 10).
     * @param sortBy The field by which the product list should be sorted (default is "name").
     * @param order The sorting order: "asc" for ascending or "desc" for descending (default is "asc").
     * @param model The Model object used to add attributes to the view.
     * @return The name of the view template to display the product list ("manager/product/index").
     */
    @GetMapping
    public String index(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order,
            Model model) {

        Pageable pageable = null;

        // Create a Pageable object based on the provided page, size, sortBy, and order
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        // Get the paginated list of products
        Page<ProductDTO> products = productService.searchAll(keyword, pageable);

        // Add all attributes to the model
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("isShow", true);
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);

        // Calculate the page numbers
        List<Integer> pageNumbers = IntStream
                .range(Math.max(page - pageLimit, 0), Math.min(page + pageLimit + 1, products.getTotalPages()))
                .boxed()
                .toList();

        // Add the page numbers to the model
        model.addAttribute("pageNumbers", pageNumbers);

        // Calculate the page info
        String pageInfo = String.format("%d - %d of %d items",
                Math.min(page * size + 1, products.getTotalElements()),
                Math.min((page + 1) * size, products.getTotalElements()), products.getTotalElements());

        // Add the page info to the model
        model.addAttribute("pageInfo", pageInfo);

        // Return the name of the view template
        return "manager/product/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        // Create new productCreateUpdateDTO
        var productCreateUpdateDTO = new ProductCreateUpdateDTO();

        // Get all categories
        var categories = categoryService.findAll();

        // Passing to view to validate data
        model.addAttribute("product", productCreateUpdateDTO);
        model.addAttribute("categories", categories);
        return "manager/product/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("product") @Valid ProductCreateUpdateDTO productCreateUpdateDTO,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // Validate data from form
        if (bindingResult.hasErrors()) {
            return "manager/product/create";
        }

        // Create product
        ProductDTO result = null;
        try {
            result = productService.create(productCreateUpdateDTO);
        } catch (Exception e) {
            // Passing error message to view creating
            model.addAttribute("error", e.getMessage());
            return "manager/product/create";
        }

        if (result != null) {
            // Redirect to index of products with success message
            redirectAttributes.addFlashAttribute("success", "Create product successfully");
            return "redirect:/manager/product";
        } else {
            // Passing error message to view creating
            model.addAttribute("error", "Create product failed");
            return "manager/product/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable UUID id,
            Model model) {
        // Get product from db
        var productDTO = productService.findById(id);

        // Get all categories
        var categories = categoryService.findAll();

        // Passing to view to validate data
        model.addAttribute("product", productDTO);
        model.addAttribute("categories", categories);
        return "manager/product/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable UUID id,
            @ModelAttribute("product") @Valid ProductCreateUpdateDTO productCreateUpdateDTO,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // Validate data from form
        if (bindingResult.hasErrors()) {
            return "manager/product/edit";
        }

        // Update product
        ProductDTO result = null;
        try {
            result = productService.update(id, productCreateUpdateDTO);
        } catch (Exception e) {
            // Passing error message to view creating
            model.addAttribute("error", e.getMessage());
            model.addAttribute("product", productCreateUpdateDTO);
            return "manager/product/edit";
        }

        if (result != null) {
            // Redirect to index of products with success message
            redirectAttributes.addFlashAttribute("success", "Update product successfully");
            return "redirect:/manager/product";
        } else {
            // Passing error message to view update
            model.addAttribute("error", "Update product failed");
            return "manager/product/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id,
                         RedirectAttributes redirectAttributes) {
        boolean result = false;
        try {
            result = productService.delete(id);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Delete product failed");
            return "redirect:/manager/product";
        }

        if (result) {
            // Redirect to index of products
            redirectAttributes.addFlashAttribute("success",
                    "Delete product successfully");
        } else {
            // Passing error message to index
            redirectAttributes.addFlashAttribute("error",
                    "Delete product failed");
        }
        return "redirect:/manager/product";
    }
}
