<<<<<<< HEAD
package services.interfaces;

import models.PublishingHouse;
import services.interfaces.common.ICommonService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IPublishingHouseService extends ICommonService<PublishingHouse, Long> {

    PublishingHouse getByName(String name);

    Set<PublishingHouse> findAllByNameIn(List<String> names);

    PublishingHouse updateFromJson(Long Id, String jsonPublishingHouse, String booksFlag) throws IOException;

}
=======
package services.interfaces;

import models.PublishingHouse;
import services.interfaces.common.ICommonService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IPublishingHouseService extends ICommonService<PublishingHouse, Long> {

    PublishingHouse getByName(String name);

    Set<PublishingHouse> findAllByNameIn(List<String> names);

    PublishingHouse updateFromJson(Long Id, String jsonPublishingHouse, String booksFlag) throws IOException;

}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
