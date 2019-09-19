package lib_group.library.services.implementations;

import lib_group.library.models.Description;
import lib_group.library.repositories.DescriptionRepository;
import lib_group.library.services.interfaces.IDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DescriptionService implements IDescriptionService {
    @Autowired
    private DescriptionRepository descriptionRepository;

    public void deleteAll() {
        descriptionRepository.deleteAll();
    }

    @Override
    public ResponseEntity save(Description obj) {
        return null;
    }

    public List<Description> getAll() {
        return descriptionRepository.findAll();
    }

    public ResponseEntity delete(String id) {
        if (id != null) {
            descriptionRepository.deleteById(id);
        }
        return null;
    }

    @Override
    public ResponseEntity getById(String Id) {
        return null;
    }

    public Description findById(String id) {
        return descriptionRepository.findById(id).orElse(null);
    }

    public Description saveDesc(Description description) {
        return descriptionRepository.save(description);
    }

    public List<Description> saveAll(Set<Description> objects) {
        return descriptionRepository.saveAll(objects);
    }
}
