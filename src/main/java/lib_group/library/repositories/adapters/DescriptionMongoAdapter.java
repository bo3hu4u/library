package lib_group.library.repositories.adapters;

import lib_group.library.models.Description;
import lib_group.library.repositories.DescriptionRepository;
import lib_group.library.repositories.adapters.common.CommonMongoRepositoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

public class DescriptionMongoAdapter extends CommonMongoRepositoryAdapter<Description, String> {

    private DescriptionRepository descRepository;

    public DescriptionMongoAdapter(MongoRepository<Description, String> mongoRepository) {
        super(mongoRepository);
        this.descRepository = (DescriptionRepository) mongoRepository;
    }

    public Description findByBookId(Long Id) {
        return descRepository.findByBookId(Id);
    }

    public Boolean existsByBookId(Long Id) {
        return descRepository.existsByBookId(Id);
    }
}
