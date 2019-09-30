<<<<<<< HEAD
package repositories.adapters;

import models.Description;
import repositories.DescriptionRepository;
import repositories.adapters.common.CommonMongoRepositoryAdapter;
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

    public void deleteByBookId(Long Id) {
        descRepository.deleteByBookId(Id);
    }
}
=======
package repositories.adapters;

import models.Description;
import repositories.DescriptionRepository;
import repositories.adapters.common.CommonMongoRepositoryAdapter;
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

    public void deleteByBookId(Long Id) {
        descRepository.deleteByBookId(Id);
    }
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
