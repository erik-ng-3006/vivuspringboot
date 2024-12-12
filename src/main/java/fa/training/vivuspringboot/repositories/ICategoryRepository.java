package fa.training.vivuspringboot.repositories;

import fa.training.vivuspringboot.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ICategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
    //JPA query to find category by name
    Category findByName(String name);
}
