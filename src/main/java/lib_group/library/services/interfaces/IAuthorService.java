package lib_group.library.services.interfaces;

import lib_group.library.models.Author;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public interface IAuthorService extends LibraryService<Author,Long> {
    ResponseEntity findByName(String authorName);

    ResponseEntity updateFromParams(Long Id, MultiValueMap<String, String> params);
}
