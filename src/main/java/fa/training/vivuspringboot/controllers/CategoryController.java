package fa.training.vivuspringboot.controllers;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

import fa.training.vivuspringboot.dtos.category.CategoryCreateUpdateDTO;
import fa.training.vivuspringboot.dtos.category.CategoryDTO;
import fa.training.vivuspringboot.services.ICategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/manager/category")
public class CategoryController {
    @Value("${spring.data.web.pageable.pageLimit}")
    private Integer pageLimit;
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order,
            Model model) {
        Pageable pageable = null;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        Page<CategoryDTO> categories = categoryService.searchAll(keyword, pageable);
        model.addAttribute("categories", categories.getContent());
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("totalElements", categories.getTotalElements());
        model.addAttribute("isShow", true);
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        List<Integer> pageNumbers = IntStream
                .range(Math.max(page - pageLimit, 0), Math.min(page + pageLimit + 1, categories.getTotalPages()))
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
        String pageInfo = String.format("%d - %d of %d items",
                Math.min(page * size + 1, categories.getTotalElements()),
                Math.min((page + 1) * size, categories.getTotalElements()), categories.getTotalElements());
        model.addAttribute("pageInfo", pageInfo);
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

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable UUID id,
            Model model) {
        // Get category from db
        var categoryDTO = categoryService.findById(id);

        // Passing to view to validate data
        model.addAttribute("category", categoryDTO);
        return "manager/category/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable UUID id,
            @ModelAttribute("category") @Valid CategoryCreateUpdateDTO categoryCreateUpdateDTO,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // Validate data from form
        if (bindingResult.hasErrors()) {
            return "manager/category/edit";
        }

        // Update category
        CategoryDTO result = null;
        try {
            result = categoryService.update(id, categoryCreateUpdateDTO);
        } catch (Exception e) {
            // Passing error message to view creating
            model.addAttribute("error", e.getMessage());
            model.addAttribute("category", categoryCreateUpdateDTO);
            return "manager/category/edit";
        }

        if (result != null) {
            // Redirect to index of categories with success message
            redirectAttributes.addFlashAttribute("success", "Update category successfully");
            return "redirect:/manager/category";
        } else {
            // Passing error message to view update
            model.addAttribute("error", "Update category failed");
            return "manager/category/edit";
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
        } else {
            // Passing error message to index
            redirectAttributes.addFlashAttribute("error",
                    "Delete category failed");
        }
        return "redirect:/manager/category";
    }
}
