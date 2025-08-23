package re1kur.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import re1kur.app.entity.Rental;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentalRepository extends CrudRepository<Rental, UUID> {
    @Query(value = """
                SELECT * FROM rentals r
                WHERE r.user_id = :userId
                AND (:carId IS NULL OR r.car_id = :carId)
                AND (:someDate BETWEEN r.start_date AND r.end_date)
            """, nativeQuery = true)
    Page<Rental> findAllByUserIdAndDate(
            Pageable pageable,
            @Param("userId") UUID userId,
            @Param("carId") Integer carId,
            @Param("someDate") LocalDate date);

    @Query(value = """
                SELECT * FROM rentals r
                WHERE r.user_id = :userId
                AND (:carId IS NULL OR r.car_id = :carId)
            """, nativeQuery = true)
    Page<Rental> findAllByUserId(
            Pageable pageable,
            @Param("userId") UUID userId,
            @Param("carId") Integer carId);

    @Query(value = """
                SELECT * FROM rentals r
                WHERE (:userId IS NULL OR r.user_id = :userId)
                  AND (:carId IS NULL OR r.car_id = :carId)
                  AND (:someDate BETWEEN r.start_date AND r.end_date)
            """, nativeQuery = true)
    Page<Rental> findAllByUserIdAndCarIdAndDate(
            Pageable pageable,
            @Param("userId") UUID userId,
            @Param("carId") Integer carId,
            @Param("someDate") Date someDate);

    @Query(value = """
                SELECT * FROM rentals r
                WHERE (:userId IS NULL OR r.user_id = :userId)
                  AND (:carId IS NULL OR r.car_id = :carId)
            """, nativeQuery = true)
    Page<Rental> findAllByUserIdAndCarId(
            Pageable pageable,
            @Param("userId") UUID userId,
            @Param("carId") Integer carId);

    @Query(value = """
            SELECT r.car.id FROM Rental r WHERE r.userId = :userId
            """)
    List<Integer> findAllCarIdsByUserId(
            @Param("userId") UUID userId);

    @Query(value = """
            SELECT COUNT(r) > 0
            FROM Rental r
            WHERE r.car.id = :carId
              AND :startDate <= r.endDate
              AND :endDate >= r.startDate
            """)
    boolean existsByCarIdAndDate(@Param("carId") Integer carId,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);
}
