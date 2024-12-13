package fa.training.vivuspringboot.repositories;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import fa.training.vivuspringboot.entities.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IUserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);
}
