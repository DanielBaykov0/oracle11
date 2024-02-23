package baykov.daniel.oracle11.repository;

import baykov.daniel.oracle11.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
