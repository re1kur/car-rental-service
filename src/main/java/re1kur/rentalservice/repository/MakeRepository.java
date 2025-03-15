package re1kur.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.rentalservice.entity.Make;

@Repository
public interface MakeRepository extends JpaRepository<Make, Integer> {
}
