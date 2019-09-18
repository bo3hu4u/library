package lib_group.library.services;

import lib_group.library.models.Description;
import lib_group.library.repositories.DescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DescriptionService {
    @Autowired
    private DescriptionRepository descriptionRepository;

    public void deleteAll() {
        descriptionRepository.deleteAll();
    }

    public List<Description> getAll() {
        return descriptionRepository.findAll();
    }

    public void deleteById(String id) {
        if (id != null) {
            descriptionRepository.deleteById(id);
        }
    }

    public Description findById(String id) {
        return descriptionRepository.findById(id).orElse(null);
    }

    public Description save(Description description) {
        return descriptionRepository.save(description);
    }

}
