package fa.training.vivuspringboot.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
public class CategoryCreateUpdateDTO {
    private UUID id;

    @NotBlank(message = "Category name is required")
    @Length(min = 2, max = 255, message = "Category name must be between 2 and 255 characters")
    private String name;

    @Length(max = 500, message = "Category description must be less than 500 characters")
    private String description;

    @NotNull(message = "Active is required")
    private boolean active;
}
