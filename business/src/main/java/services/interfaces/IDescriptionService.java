package services.interfaces;

import models.Description;
import services.interfaces.common.ICommonService;

public interface IDescriptionService extends ICommonService<Description, String> {
    Description getByBookId(Long Id);

    Boolean existsByBookId(Long Id);

    void deleteByBookId(Long Id);
}
