package lib_group.library.services.interfaces;

import lib_group.library.models.Location;
import lib_group.library.services.interfaces.common.ICommonService;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ILocationService extends ICommonService<Location,Long> {
    Location getByAddress(String address);

    Location updateLocation(Long Id, String json) throws IOException;
}
