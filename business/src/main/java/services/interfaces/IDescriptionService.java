<<<<<<< HEAD
package services.interfaces;

import models.Description;
import services.interfaces.common.ICommonService;

public interface IDescriptionService extends ICommonService<Description, String> {
    Description getByBookId(Long Id);

    Boolean existsByBookId(Long Id);

    void deleteByBookId(Long Id);
}
=======
package services.interfaces;

import models.Description;
import services.interfaces.common.ICommonService;

public interface IDescriptionService extends ICommonService<Description, String> {
    Description getByBookId(Long Id);

    Boolean existsByBookId(Long Id);

    void deleteByBookId(Long Id);
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
