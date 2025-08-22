package re1kur.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re1kur.app.entity.Make;

@Repository
public interface MakeRepository extends JpaRepository<Make, Integer> {
    boolean existsByName(String name);

    @Query(value = """
            SELECT m FROM Make m
            WHERE
            (LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL)
            """)
    Page<Make> findAll(
            Pageable pageable,
            @Param("name") String name);
}
