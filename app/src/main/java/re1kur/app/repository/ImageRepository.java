package re1kur.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import re1kur.app.entity.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, String> {
}
