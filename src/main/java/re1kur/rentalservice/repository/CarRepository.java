package re1kur.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re1kur.rentalservice.entity.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {
}
