package services.interfaces;

import models.Author;
import services.interfaces.common.ICommonService;
import org.springframework.util.MultiValueMap;

public interface IAuthorService extends ICommonService<Author,Long> {
    Author findByName(String authorName);

    Author updateFromParams(Long Id, MultiValueMap<String, String> params);
}
