package fa.training.vivuspringboot.controllers;

import fa.training.vivuspringboot.dtos.category.CategoryDTO;
import fa.training.vivuspringboot.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/manager/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping
    public String findAll(Model model) {
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "manager/category/index";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id, Model model) {
        CategoryDTO category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "manager/category/detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = false;
        try {
            isDeleted = categoryService.delete(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete category");
            return "redirect:/manager/category";
        }

        if (isDeleted) {
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Category not found");
        }
        return "redirect:/manager/category";
    }

}
