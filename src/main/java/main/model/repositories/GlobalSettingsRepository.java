package main.model.repositories;

import java.util.Optional;
import main.model.tables.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Integer> {
  Optional<GlobalSettings> findByCode(String code);
}
