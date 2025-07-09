package re1kur.app.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.app.entity.car.Make;

@Repository
public interface MakeRepository extends JpaRepository<Make, Integer> {
    boolean existsByName(String name);
}
