<<<<<<< HEAD
package controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.NotFoundEntityException;
import models.Location;
import services.interfaces.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class LocationController {

    @Autowired
    private ILocationService locationService;

    @GetMapping(value = "/addresses")
    public List<Location> getAllAddresses() {
        return locationService.getAll();
    }

    @PostMapping(value = "/addresses")
    public ResponseEntity postAddress(@RequestBody @Valid Location location) {
        try {
            return ResponseEntity.ok(locationService.save(location));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

    @GetMapping(value = "/addresses/{Id}")
    public ResponseEntity getAddressById(@PathVariable Long Id) {
        try {
            return ResponseEntity.ok(locationService.getById(Id));
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

    @PutMapping(value = "addresses/{Id}")
    public ResponseEntity changeAddress(@PathVariable Long Id, @RequestBody String addressChanges) throws IOException {
        try {
            return ResponseEntity.ok(locationService.updateLocation(Id, addressChanges));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (InvalidFormatException | NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }

    }

    @DeleteMapping(value = "/addresses/{Id}")
    public ResponseEntity removeAddress(@PathVariable("Id") Long Id) {
        try {
            locationService.delete(Id);
            return ResponseEntity.ok(locationService.getAll());
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

}
=======
package controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.NotFoundEntityException;
import models.Location;
import services.interfaces.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class LocationController {

    @Autowired
    private ILocationService locationService;

    @GetMapping(value = "/addresses")
    public List<Location> getAllAddresses() {
        return locationService.getAll();
    }

    @PostMapping(value = "/addresses")
    public ResponseEntity postAddress(@RequestBody @Valid Location location) {
        try {
            return ResponseEntity.ok(locationService.save(location));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
        }
    }

    @GetMapping(value = "/addresses/{Id}")
    public ResponseEntity getAddressById(@PathVariable Long Id) {
        try {
            return ResponseEntity.ok(locationService.getById(Id));
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

    @PutMapping(value = "addresses/{Id}")
    public ResponseEntity changeAddress(@PathVariable Long Id, @RequestBody String addressChanges) throws IOException {
        try {
            return ResponseEntity.ok(locationService.updateLocation(Id, addressChanges));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (InvalidFormatException | NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }

    }

    @DeleteMapping(value = "/addresses/{Id}")
    public ResponseEntity removeAddress(@PathVariable("Id") Long Id) {
        try {
            locationService.delete(Id);
            return ResponseEntity.ok(locationService.getAll());
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
