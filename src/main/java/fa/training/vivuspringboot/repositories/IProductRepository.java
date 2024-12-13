package fa.training.vivuspringboot.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import fa.training.vivuspringboot.entities.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Product findByName(String name);
}