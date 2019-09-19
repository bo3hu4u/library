package lib_group.library.services;

import lib_group.library.LibraryApplicationTests;
import lib_group.library.models.Location;
import lib_group.library.models.PublishingHouse;
import lib_group.library.repositories.LocationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class LocationServiceTest extends LibraryApplicationTests {
    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void getAllLocations() {
        List<Location> locationsFromService = locationService.getAll();
        assertEquals(5, locationsFromService.size());
    }

    @Test
    public void getLocationByIdExists() {
        PublishingHouse publishingHouseTest = new PublishingHouse(2L, "Publish2");
        Location locationTest = new Location(2L, "address2");
        locationTest.setPublishingHouse(publishingHouseTest);

        Location locationFromService = (Location) locationService.getById(2L).getBody();

        assertEquals(locationTest.getPublishingHouse().getPublishHouseId(), locationFromService.getPublishingHouse().getPublishHouseId());
        assertEquals(locationTest, locationFromService);
    }

    @Test
    public void getLocationByIdNotExists() {
        Long id = 133333L;

        ResponseEntity responseEntity = locationService.getById(id);
        ResponseEntity testResponseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find location with id 133333");
        assertEquals(responseEntity, testResponseEntity);
    }

    @Test
    public void postLocationWithFreePublishingHouse() throws Exception {
        PublishingHouse publishingHouseTest = new PublishingHouse(5L, "Publish5");
        Location locationTest = new Location(6L, "testLocation");
        locationTest.setPublishingHouse(publishingHouseTest);

        Location locationToAdd = new Location("testLocation");
        PublishingHouse publishingHouseToAdd = new PublishingHouse("Publish5");
        locationToAdd.setPublishingHouse(publishingHouseToAdd);
        Location locationFromService = (Location) locationService.save(locationToAdd).getBody();

        assertEquals(locationTest.getPublishingHouse().getPublishHouseId(), locationFromService.getPublishingHouse().getPublishHouseId());
        assertEquals(locationTest, locationFromService);
    }

    @Test
    public void postLocationAddress() {
        Location locationTest = new Location(6L, "testLocation");

        Location locationFromService = (Location) locationService.save(new Location("testLocation")).getBody();
        assertEquals(null, locationFromService.getPublishingHouse());
        assertEquals(locationTest, locationFromService);
    }


    @Test
    public void putLocationAddress() throws Exception {
        Location locationTest = new Location(2L, "address2Changed");

        String json = "{\"address\":\"address2Changed\"}";
        Location locationFromService = (Location) locationService.updateLocation(2L, json).getBody();

        assertEquals("address2Changed", locationFromService.getAddress());
        assertEquals(locationTest, locationFromService);
    }

    @Transactional
    @Test
    public void putLocationPublishingHouseExist() throws Exception {
        PublishingHouse publishingHouseTest = new PublishingHouse(5L, "Publish5");
        Location locationTest = new Location(2L, "address2");
        locationTest.setPublishingHouse(publishingHouseTest);

        String json = "{\"publishingHouse\":{\"name\":\"Publish5\"}}";
        Location locationFromService = (Location) locationService.updateLocation(2L, json).getBody();

        assertEquals(locationTest.getPublishingHouse().getPublishHouseId(), locationFromService.getPublishingHouse().getPublishHouseId());
    }

    @Test
    public void putLocationPublishingHouseNull() throws Exception {
        Location locationTest = new Location(2L, "address2");

        String json = "{\"publishingHouse\":null}";
        Location locationFromService = (Location) locationService.updateLocation(2L, json).getBody();
        assertEquals(null, locationFromService.getPublishingHouse());
        assertEquals(locationTest, locationFromService);
    }

    @Test
    public void deleteLocationExists() {

        locationService.delete(2L);

        ResponseEntity testResponseEntityAddress = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find location with id 2");
        ResponseEntity responseEntityAddress = locationService.getById(2L);
        assertEquals(responseEntityAddress, testResponseEntityAddress);

        ResponseEntity testResponseEntityPH = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find publishing house with id 2");
        ResponseEntity responseEntityPH = publishingHouseService.getById(2L);
        assertNotEquals(responseEntityPH, testResponseEntityPH);
    }

    @Test
    public void deleteLocationNotExists() {
        Long id = 133333L;
        ResponseEntity responseEntity = locationService.getById(id);
        ResponseEntity testResponseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find location with id 133333");
        assertEquals(responseEntity, testResponseEntity);

    }


}