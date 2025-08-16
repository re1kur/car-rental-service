package re1kur.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re1kur.app.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
}
