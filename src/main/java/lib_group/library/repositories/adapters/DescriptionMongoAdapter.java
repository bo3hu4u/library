package lib_group.library.repositories.adapters;

import lib_group.library.models.Description;
import lib_group.library.repositories.adapters.common.CommonMongoRepositoryAdapter;
import org.springframework.data.mongodb.repository.MongoRepository;

public class DescriptionMongoAdapter extends CommonMongoRepositoryAdapter<Description, String> {
    public DescriptionMongoAdapter(MongoRepository<Description, String> mongoRepository) {
        super(mongoRepository);
    }
}
