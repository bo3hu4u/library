package lib_group.library.services.interfaces;

import lib_group.library.models.Description;
import lib_group.library.services.interfaces.common.ICommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

public interface IDescriptionService extends ICommonService<Description, String> {

}
