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
