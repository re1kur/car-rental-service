package re1kur.app.repository;

import org.springframework.data.repository.CrudRepository;
import re1kur.app.entity.make.MakeInformation;

public interface MakeInformationRepository extends CrudRepository<MakeInformation, Integer> {
}
