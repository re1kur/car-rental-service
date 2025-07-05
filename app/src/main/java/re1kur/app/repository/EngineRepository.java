package re1kur.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import re1kur.app.core.dto.EngineDto;
import re1kur.app.entity.Engine;

@Repository
public interface EngineRepository extends CrudRepository<Engine, Integer> {
    boolean existsByName(String name);

    Page<Engine> findAll(Pageable pageable);
}
