package lib_group.library.services.interfaces;

import lib_group.library.models.Location;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ILocationService extends LibraryService<Location,Long> {
    Location getByAddress(String address);

    ResponseEntity updateLocation(Long Id, String json) throws IOException;
}
