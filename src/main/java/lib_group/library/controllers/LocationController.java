package lib_group.library.controllers;

import lib_group.library.models.Location;
import lib_group.library.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping(value = "/addresses")
    public List<Location> getAllAddresses() {
        return locationService.getAll();
    }

    @PostMapping(value = "/addresses")
    public ResponseEntity postAddress(@RequestBody @Valid Location location) {
        return locationService.save(location);
    }

    @GetMapping(value = "/addresses/{Id}")
    public ResponseEntity getAddressById(@PathVariable Long Id) {
        return locationService.getById(Id);
    }

    @PutMapping(value = "addresses/{Id}")
    public ResponseEntity changeAddress(@PathVariable Long Id, @RequestBody String addressChanges) throws IOException {
        return locationService.changeLocation(Id, addressChanges);
    }

    @DeleteMapping(value = "/addresses/{Id}")
    public ResponseEntity removeAddress(@PathVariable("Id") Long Id) {
        ResponseEntity deletedLocationResponseEntity = locationService.delete(Id);
        if (deletedLocationResponseEntity.getStatusCode().is4xxClientError()) {
            return deletedLocationResponseEntity;
        } else {
            return ResponseEntity.ok(locationService.getAll());
        }
    }

}
