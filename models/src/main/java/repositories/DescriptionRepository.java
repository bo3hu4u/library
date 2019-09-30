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
