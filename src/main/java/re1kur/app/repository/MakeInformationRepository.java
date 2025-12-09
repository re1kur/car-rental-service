package re1kur.app.repository;

import org.springframework.data.repository.CrudRepository;
import re1kur.app.model.entity.MakeInformation;

public interface MakeInformationRepository extends CrudRepository<MakeInformation, Integer> {
}
