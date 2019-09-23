package lib_group.library.services.implementations;

import lib_group.library.models.Description;
import lib_group.library.repositories.DescriptionRepository;
import lib_group.library.repositories.adapters.DescriptionMongoAdapter;
import lib_group.library.services.implementations.common.CommonServiceImpl;
import lib_group.library.services.interfaces.IDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class DescriptionServiceImpl extends CommonServiceImpl<Description, String> implements IDescriptionService {

    private DescriptionMongoAdapter descriptionRepositoryAdapter;

    public DescriptionServiceImpl(@Autowired DescriptionRepository descriptionRepository) {
        this.descriptionRepositoryAdapter = new DescriptionMongoAdapter(descriptionRepository);
    }

    @Override
    public JpaRepository<Description, String> getRepository() {
        return descriptionRepositoryAdapter;
    }

    @Override
    public Class<Description> getCurrentClass() {
        return Description.class;
    }

    public void delete(String id) {
        if (id != null) {
            descriptionRepositoryAdapter.deleteById(id);
        }
    }


}
