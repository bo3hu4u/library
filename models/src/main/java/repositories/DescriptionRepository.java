<<<<<<< HEAD
package repositories;


import models.Description;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends MongoRepository<Description, String> {
    Description findByBookId(Long id);

    Boolean existsByBookId(Long Id);

    void deleteByBookId(Long Id);
}
=======
package repositories;


import models.Description;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends MongoRepository<Description, String> {
    Description findByBookId(Long id);

    Boolean existsByBookId(Long Id);

    void deleteByBookId(Long Id);
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
