<<<<<<< HEAD
package services.interfaces;

import models.Location;
import services.interfaces.common.ICommonService;

import java.io.IOException;

public interface ILocationService extends ICommonService<Location,Long> {
    Location getByAddress(String address);

    Location updateLocation(Long Id, String json) throws IOException;
}
=======
package services.interfaces;

import models.Location;
import services.interfaces.common.ICommonService;

import java.io.IOException;

public interface ILocationService extends ICommonService<Location,Long> {
    Location getByAddress(String address);

    Location updateLocation(Long Id, String json) throws IOException;
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
