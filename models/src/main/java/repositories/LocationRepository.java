<<<<<<< HEAD
package repositories;

import models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByAddress(String address);
}
=======
package repositories;

import models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByAddress(String address);
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
