package fa.training.vivuspringboot.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import fa.training.vivuspringboot.entities.Product;
public interface IProductRepository extends JpaRepository<Product, UUID> {
    Product findByName(String name);
}