package fa.training.vivuspringboot.repositories;

import fa.training.vivuspringboot.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICategoryRepository extends JpaRepository<Category, UUID> {
    //JPA query to find category by name
    Category findByName(String name);
}
