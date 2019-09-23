package lib_group.library.repositories;


import lib_group.library.models.Description;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface DescriptionRepository extends MongoRepository<Description, String> {
}
