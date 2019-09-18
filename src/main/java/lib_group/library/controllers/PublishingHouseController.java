package lib_group.library.controllers;

import lib_group.library.models.PublishingHouse;
import lib_group.library.services.PublishingHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class PublishingHouseController {
    @Autowired
    PublishingHouseService publishingHouseService;

    @GetMapping(value = "/publish_houses")
    public List<PublishingHouse> getAllPublishingHouses() {
        return publishingHouseService.getAll();
    }

    @PostMapping(value = "/publish_houses")
    public ResponseEntity addNewPublishingHouse(@RequestBody @Valid PublishingHouse publishingHouse) {
        return publishingHouseService.save(publishingHouse);
    }

    @GetMapping(value = "/publish_houses/{Id}")
    public ResponseEntity getPublishingHouse(@PathVariable Long Id) {
        return publishingHouseService.getById(Id);
    }

    @PutMapping(value = "/publish_houses/{Id}")
    @ResponseBody
    public ResponseEntity changePublishingHouse(@PathVariable("Id") Long Id, @RequestBody String json, @RequestParam(required = false) String booksFlag) throws IOException {
        return publishingHouseService.changeFromJson(Id, json, booksFlag);
    }

    @DeleteMapping(value = "/publish_houses/{Id}")
    public ResponseEntity removePublishingHouse(@PathVariable("Id") Long Id) {
        ResponseEntity deletedPublishingHouseResponseEntity = publishingHouseService.delete(Id);
        if (deletedPublishingHouseResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return deletedPublishingHouseResponseEntity;
        } else {
            return ResponseEntity.ok(publishingHouseService.getAll());
        }
    }

}
