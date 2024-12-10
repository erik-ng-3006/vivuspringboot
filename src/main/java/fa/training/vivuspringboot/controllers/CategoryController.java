package fa.training.vivuspringboot.controllers;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fa.training.vivuspringboot.dtos.category.CategoryCreateUpdateDTO;
import fa.training.vivuspringboot.dtos.category.CategoryDTO;
import fa.training.vivuspringboot.services.ICategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/manager/category")
public class CategoryController {
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String findAll(Model model) {
        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "manager/category/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        // Create new categoryCreateUpdateDTO
        var categoryCreateUpdateDTO = new CategoryCreateUpdateDTO();

        // Passing to view to validate data
        model.addAttribute("category", categoryCreateUpdateDTO);
        return "manager/category/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("category") @Valid CategoryCreateUpdateDTO categoryCreateUpdateDTO,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // Validate data from form
        if (bindingResult.hasErrors()) {
            return "manager/category/create";
        }

        // Create category
        CategoryDTO result = null;
        try {
            result = categoryService.create(categoryCreateUpdateDTO);
        } catch (Exception e) {
            // Passing error message to view creating
            model.addAttribute("error", e.getMessage());
            return "manager/category/create";
        }

        if (result != null) {
            // Redirect to index of categories with success message
            redirectAttributes.addFlashAttribute("success", "Create category successfully");
            return "redirect:/manager/category";
        } else {
            // Passing error message to view creating
            model.addAttribute("error", "Create category failed");
            return "redirect:/manager/category/create";
        }
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id,
                         RedirectAttributes redirectAttributes) {
        boolean result = false;
        try {
            result = categoryService.delete(id);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Delete category failed");
            return "redirect:/manager/category";
        }

        if (result) {
            // Redirect to index of categories
            redirectAttributes.addFlashAttribute("success",
                    "Delete category successfully");
            return "redirect:/manager/category";
        } else {
            // Passing error message to index
            redirectAttributes.addFlashAttribute("error",
                    "Delete category failed");
            return "redirect:/manager/category";
        }
    }
}
