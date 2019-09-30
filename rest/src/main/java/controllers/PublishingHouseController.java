package controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.NotFoundEntityException;
import models.PublishingHouse;
import services.interfaces.IPublishingHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class PublishingHouseController {
    @Autowired
    private IPublishingHouseService publishingHouseService;

    @GetMapping(value = "/publish_houses")
    public List<PublishingHouse> getAllPublishingHouses() {
        return publishingHouseService.getAll();
    }

    @PostMapping(value = "/publish_houses")
    public ResponseEntity addNewPublishingHouse(@RequestBody @Valid PublishingHouse publishingHouse) {
        try {
            return ResponseEntity.ok(publishingHouseService.save(publishingHouse));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

    @GetMapping(value = "/publish_houses/{Id}")
    public ResponseEntity getPublishingHouse(@PathVariable Long Id) {
        try {
            return ResponseEntity.ok(publishingHouseService.getById(Id));
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

    @PutMapping(value = "/publish_houses/{Id}")
    @ResponseBody
    public ResponseEntity changePublishingHouse(@PathVariable("Id") Long Id, @RequestBody String json, @RequestParam(required = false) String booksFlag) throws IOException {
        try {
            return ResponseEntity.ok(publishingHouseService.updateFromJson(Id, json, booksFlag));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (InvalidFormatException | NotFoundEntityException ifException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ifException.getMessage());
        }

    }

    @DeleteMapping(value = "/publish_houses/{Id}")
    public ResponseEntity removePublishingHouse(@PathVariable("Id") Long Id) {
        try {
            publishingHouseService.delete(Id);
            return ResponseEntity.ok(publishingHouseService.getAll());
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

}
