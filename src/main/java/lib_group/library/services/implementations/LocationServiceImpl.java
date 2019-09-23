package lib_group.library.services.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lib_group.library.exceptions.NotFoundEntityException;
import lib_group.library.models.Location;
import lib_group.library.models.PublishingHouse;
import lib_group.library.repositories.LocationRepository;
import lib_group.library.services.implementations.common.CommonServiceImpl;
import lib_group.library.services.interfaces.ILocationService;
import lib_group.library.services.interfaces.IPublishingHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LocationServiceImpl extends CommonServiceImpl<Location, Long> implements ILocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private IPublishingHouseService publishingHouseService;

    @Override
    public JpaRepository<Location, Long> getRepository() {
        return locationRepository;
    }

    @Override
    public Class<Location> getCurrentClass() {
        return Location.class;
    }

    public Location getByAddress(String address) {
        return locationRepository.findByAddress(address);
    }

    public void delete(Long Id) {
        Location location = getById(Id);
        if (location.getPublishingHouse() != null) {
            location.getPublishingHouse().setLocation(null);
            publishingHouseService.save(location.getPublishingHouse());
            location.setPublishingHouse(null);
        }
        locationRepository.delete(location);
    }

    @Override
    public Location save(Location location) {
        if (location.getPublishingHouse() != null) {
            setLocationToPublishingHouse(location, location.getPublishingHouse());
        }
        return locationRepository.save(location);
    }

    private void setLocationToPublishingHouse(Location location, PublishingHouse desiredPublishingHouse) {
        if (location.getPublishingHouse() != null) {
            location.getPublishingHouse().setLocation(null);
        }
        if (desiredPublishingHouse != null) {
            try {
                PublishingHouse publishingHouseByName = publishingHouseService.getByName(desiredPublishingHouse.getName());
                location.setPublishingHouse(publishingHouseByName);
                publishingHouseByName.setLocation(location);
            } catch (NotFoundEntityException exc) {
                location.setPublishingHouse(null);
            }
        }
    }

    public Location updateLocation(Long Id, String jsonLocation) throws IOException {

        Location location = getById(Id);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<Object>> map = mapper.readValue(jsonLocation, new TypeReference<Map<String, Object>>() {
        });
        Location locationChanges = mapper.readValue(jsonLocation, Location.class);

        if (map.containsKey("address")) {
            location.setAddress(locationChanges.getAddress());
        }
        if (map.containsKey("publishingHouse")) {
            if (locationChanges.getPublishingHouse() == null) {
                if (location.getPublishingHouse() != null) {
                    location.getPublishingHouse().setLocation(null);
                    location.setPublishingHouse(null);
                }
            } else {
                setLocationToPublishingHouse(location, locationChanges.getPublishingHouse());
            }
        }
        location = locationRepository.save(location);
        return location;

    }

}
