package re1kur.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import re1kur.app.entity.Car;



public interface CarRepository extends JpaRepository<Car, Integer> {
    @Query("SELECT c FROM Car c LEFT JOIN FETCH c.make m WHERE " +
           "(LOWER(c.model) LIKE LOWER(CONCAT('%', :model, '%')) OR :model IS NULL) AND " +
           "(m.id = :makeId OR :makeId IS NULL) AND " +
           "(c.year = :year OR :year IS NULL)")
    Page<Car> findAll(
            @Param("model") String model,
            @Param("makeId") Integer makeId,
            @Param("year") Integer year,
            Pageable pageable
    );

    boolean existsByLicensePlate(String licensePlate);
}
