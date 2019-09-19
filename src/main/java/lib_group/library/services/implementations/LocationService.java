package lib_group.library.services.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lib_group.library.models.Location;
import lib_group.library.models.PublishingHouse;
import lib_group.library.repositories.LocationRepository;
import lib_group.library.services.interfaces.ILocationService;
import lib_group.library.services.interfaces.IPublishingHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LocationService implements ILocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private IPublishingHouseService publishingHouseService;

    public void deleteAll() {
        locationRepository.deleteAll();
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public ResponseEntity getById(Long Id) {
        Location location = locationRepository.findById(Id).orElse(null);
        if (location == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find location with id " + Id);
        }
        return ResponseEntity.ok(location);
    }

    public Location getByAddress(String address) {
        return locationRepository.findByAddress(address);
    }

    public ResponseEntity delete(Long Id) {
        ResponseEntity deletedLocationResponseEntity = getById(Id);
        if (deletedLocationResponseEntity.getStatusCode().is4xxClientError()) {
            return deletedLocationResponseEntity;
        } else {
            Location location = (Location) deletedLocationResponseEntity.getBody();
            if (location.getPublishingHouse() != null) {
                location.getPublishingHouse().setLocation(null);
                publishingHouseService.save(location.getPublishingHouse());
                location.setPublishingHouse(null);
            }
            locationRepository.delete(location);
            return ResponseEntity.ok().build();
        }
    }

    public ResponseEntity save(Location location) {
        try {
            if (location.getPublishingHouse() != null) {
                setLocationToPublishingHouse(location, location.getPublishingHouse());
            }
            return ResponseEntity.ok(locationRepository.save(location));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
        }
    }

    public List<Location> saveAll(Set<Location> locations) {
        return locationRepository.saveAll(locations);
    }

    private void setLocationToPublishingHouse(Location location, PublishingHouse desiredPublishingHouse) {
        if (location.getPublishingHouse() != null) {
            location.getPublishingHouse().setLocation(null);
        }
        ResponseEntity publishingHouseByNameResponseEntity = publishingHouseService.getByName(desiredPublishingHouse.getName());
        if (desiredPublishingHouse != null) {
            if (!publishingHouseByNameResponseEntity.getStatusCode().is4xxClientError()) {
                PublishingHouse publishingHouseByName = (PublishingHouse) publishingHouseByNameResponseEntity.getBody();
                if (publishingHouseByName != null) {
                    location.setPublishingHouse(publishingHouseByName);

                }
                publishingHouseByName.setLocation(location);
            } else {
                location.setPublishingHouse(null);
            }
        }
    }

    public ResponseEntity updateLocation(Long Id, String jsonLocation) throws IOException {
        ResponseEntity locationResponseEntity = getById(Id);
        if (locationResponseEntity.getStatusCode().is4xxClientError()) {
            return locationResponseEntity;
        }
        Location location = (Location) locationResponseEntity.getBody();

        try {
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
            return ResponseEntity.ok(location);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (InvalidFormatException ifException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ifException.getMessage());
        }
    }

}
