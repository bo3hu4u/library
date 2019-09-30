<<<<<<< HEAD
package repositories;

import models.PublishingHouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface PublishingHouseRepository extends JpaRepository<PublishingHouse, Long> {
    Set<PublishingHouse> findAllByNameIn(Collection<String> names);
    PublishingHouse findPublishingHouseByName(String name);
}
=======
package repositories;

import models.PublishingHouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface PublishingHouseRepository extends JpaRepository<PublishingHouse, Long> {
    Set<PublishingHouse> findAllByNameIn(Collection<String> names);
    PublishingHouse findPublishingHouseByName(String name);
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
