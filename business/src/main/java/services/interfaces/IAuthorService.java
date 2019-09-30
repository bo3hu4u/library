<<<<<<< HEAD
package services.interfaces;

import models.Author;
import services.interfaces.common.ICommonService;
import org.springframework.util.MultiValueMap;

public interface IAuthorService extends ICommonService<Author,Long> {
    Author findByName(String authorName);

    Author updateFromParams(Long Id, MultiValueMap<String, String> params);
}
=======
package services.interfaces;

import models.Author;
import services.interfaces.common.ICommonService;
import org.springframework.util.MultiValueMap;

public interface IAuthorService extends ICommonService<Author,Long> {
    Author findByName(String authorName);

    Author updateFromParams(Long Id, MultiValueMap<String, String> params);
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
