package services.interfaces;

import models.Location;
import services.interfaces.common.ICommonService;

import java.io.IOException;

public interface ILocationService extends ICommonService<Location,Long> {
    Location getByAddress(String address);

    Location updateLocation(Long Id, String json) throws IOException;
}
