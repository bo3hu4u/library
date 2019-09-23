package lib_group.library.services.interfaces;

import lib_group.library.models.PublishingHouse;
import lib_group.library.services.interfaces.common.ICommonService;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IPublishingHouseService extends ICommonService<PublishingHouse, Long> {

    PublishingHouse getByName(String name);

    Set<PublishingHouse> findAllByNameIn(List<String> names);

    PublishingHouse updateFromJson(Long Id, String jsonPublishingHouse, String booksFlag) throws IOException;

}
