package lib_group.library.services.interfaces;

import lib_group.library.models.Description;
import org.springframework.http.ResponseEntity;

public interface IDescriptionService extends LibraryService<Description, String> {
    Description findById(String id);

    Description saveDesc(Description description);

    ResponseEntity delete(String id);
}
