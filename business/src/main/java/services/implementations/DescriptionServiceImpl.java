<<<<<<< HEAD
package services.implementations;

import models.Description;
import repositories.DescriptionRepository;
import repositories.adapters.DescriptionMongoAdapter;
import services.implementations.common.CommonServiceImpl;
import services.interfaces.IDescriptionService;
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

    public Description getByBookId(Long Id) {
        return descriptionRepositoryAdapter.findByBookId(Id);
    }


    public Boolean existsByBookId(Long Id) {
        return descriptionRepositoryAdapter.existsByBookId(Id);
    }

    public void deleteByBookId(Long Id) {
        descriptionRepositoryAdapter.deleteByBookId(Id);
    }
}
=======
package services.implementations;

import models.Description;
import repositories.DescriptionRepository;
import repositories.adapters.DescriptionMongoAdapter;
import services.implementations.common.CommonServiceImpl;
import services.interfaces.IDescriptionService;
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

    public Description getByBookId(Long Id) {
        return descriptionRepositoryAdapter.findByBookId(Id);
    }


    public Boolean existsByBookId(Long Id) {
        return descriptionRepositoryAdapter.existsByBookId(Id);
    }

    public void deleteByBookId(Long Id) {
        descriptionRepositoryAdapter.deleteByBookId(Id);
    }
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
