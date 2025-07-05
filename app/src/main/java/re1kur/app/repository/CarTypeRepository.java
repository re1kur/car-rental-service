package re1kur.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import re1kur.app.entity.CarType;

@Repository
public interface CarTypeRepository extends CrudRepository<CarType, Integer> {
    boolean existsByName(String name);

    Page<CarType> findAll(Pageable pageable);
}
