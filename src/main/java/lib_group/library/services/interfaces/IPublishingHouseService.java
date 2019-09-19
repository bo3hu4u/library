package lib_group.library.services.interfaces;

import lib_group.library.models.PublishingHouse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IPublishingHouseService extends LibraryService<PublishingHouse, Long> {

    ResponseEntity getByName(String name);

    Set<PublishingHouse> findAllByNameIn(List<String> names);

    ResponseEntity updateFromJson(Long Id, String jsonPublishingHouse, String booksFlag) throws IOException;

}
