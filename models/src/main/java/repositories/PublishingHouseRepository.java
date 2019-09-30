package repositories;

import models.PublishingHouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface PublishingHouseRepository extends JpaRepository<PublishingHouse, Long> {
    Set<PublishingHouse> findAllByNameIn(Collection<String> names);
    PublishingHouse findPublishingHouseByName(String name);
}
