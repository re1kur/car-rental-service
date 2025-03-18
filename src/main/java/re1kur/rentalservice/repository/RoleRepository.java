package re1kur.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re1kur.rentalservice.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
