package re1kur.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re1kur.rentalservice.dto.car.CarReadDto;
import re1kur.rentalservice.entity.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findAllByMakeId(int id);
}
