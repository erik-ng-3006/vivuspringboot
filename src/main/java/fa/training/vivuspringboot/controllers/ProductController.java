package fa.training.vivuspringboot.controllers;

import fa.training.vivuspringboot.dtos.category.CategoryDTO;
import fa.training.vivuspringboot.dtos.product.ProductCreateUpdateDTO;
import fa.training.vivuspringboot.dtos.product.ProductDTO;
import fa.training.vivuspringboot.services.ICategoryService;
import fa.training.vivuspringboot.services.IProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/manager/product")
public class ProductController {
    private final IProductService productService;
    private final ICategoryService categoryService;

    public ProductController(IProductService productService, ICategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String findAll(Model model) {
        var products = productService.findAll();
        model.addAttribute("products", products);
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
