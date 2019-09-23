package lib_group.library.services.interfaces;

import lib_group.library.models.Author;
import lib_group.library.services.interfaces.common.ICommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public interface IAuthorService extends ICommonService<Author,Long> {
    Author findByName(String authorName);

    Author updateFromParams(Long Id, MultiValueMap<String, String> params);
}
