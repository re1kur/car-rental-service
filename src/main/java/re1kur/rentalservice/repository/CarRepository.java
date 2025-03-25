package re1kur.rentalservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import re1kur.rentalservice.entity.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {
    Page<Car> findAll(Pageable pageable);

    List<Car> findAllByMakeId(int id);
}
