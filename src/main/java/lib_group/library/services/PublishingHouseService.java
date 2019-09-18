package lib_group.library.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lib_group.library.models.Location;
import lib_group.library.models.Book;
import lib_group.library.models.PublishingHouse;
import lib_group.library.repositories.PublishingHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PublishingHouseService {
    @Autowired
    PublishingHouseRepository publishingHouseRepository;
    @Autowired
    BookService bookService;
    @Autowired
    LocationService locationService;


    public void deleteAll() {
        publishingHouseRepository.deleteAll();
    }

    public List<PublishingHouse> saveAll(Set<PublishingHouse> publishingHouses) {
        return publishingHouseRepository.saveAll(publishingHouses);
    }

    public List<PublishingHouse> getAll() {
        return publishingHouseRepository.findAll();
    }

    public ResponseEntity getById(Long Id) {
        PublishingHouse publishingHouse = publishingHouseRepository.findById(Id).orElse(null);
        if (publishingHouse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find publishing house with id " + Id);
        }
        return ResponseEntity.ok(publishingHouse);
    }

    public ResponseEntity getByName(String name) {
        PublishingHouse publishingHouse = publishingHouseRepository.findPublishingHouseByName(name);
        if (publishingHouse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find publishing house with name " + name);
        }
        return ResponseEntity.ok(publishingHouse);
    }

    public Set<PublishingHouse> findAllByNameIn(List<String> names) {
        return publishingHouseRepository.findAllByNameIn(names);
    }

    public ResponseEntity save(PublishingHouse publishingHouse) {
        try {
            if (publishingHouse.getLocation() != null) {
                Location location = locationService.getByAddress(publishingHouse.getLocation().getAddress());
                if (location != null) {
                    if (publishingHouse.getPublishHouseId() == null || !(publishingHouse.equals(location.getPublishingHouse()))) {
                        if (location.getPublishingHouse() == null) {
                            publishingHouse.setLocation(location);
                        } else {
                            location.getPublishingHouse().setLocation(null);
                            publishingHouseRepository.save(location.getPublishingHouse());
                        }
                    }
                } else {
                    Location newLocation = new Location(publishingHouse.getLocation().getAddress());
                    location = (Location) locationService.save(newLocation).getBody();
                    publishingHouse.setLocation(location);
                }
            }
            publishingHouseRepository.save(publishingHouse);
            return ResponseEntity.ok(publishingHouse);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        }
    }

    public ResponseEntity changeFromJson(Long Id, String jsonPublishingHouse, String booksFlag) throws IOException {

        ResponseEntity publishingHouseResponseEntity = getById(Id);
        if (publishingHouseResponseEntity.getStatusCode().is4xxClientError()) {
            return publishingHouseResponseEntity;
        }
        PublishingHouse publishingHouse = (PublishingHouse) publishingHouseResponseEntity.getBody();

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<Object>> map = mapper.readValue(jsonPublishingHouse, new TypeReference<Map<String, Object>>() {
            });
            PublishingHouse publishingHouseChanges = mapper.readValue(jsonPublishingHouse, PublishingHouse.class);

            List<String> bookTitles = publishingHouseChanges.getBooks().stream().filter(book -> book.getTitle() != null).map(book -> book.getTitle()).collect(Collectors.toList());
            Set<Book> books = bookService.findBooksByTitleIn(bookTitles);

            if (map.containsKey("name")) {
                publishingHouse.setName(publishingHouseChanges.getName());
            }
            if (map.containsKey("location")) {
                if (publishingHouseChanges.getLocation() == null) {
                    publishingHouse.setLocation(null);
                } else {
                    Location location = locationService.getByAddress(publishingHouseChanges.getLocation().getAddress());
                    if (location != null) {
                        publishingHouse.setLocation(location);
                    } else {
                        publishingHouse.setLocation(publishingHouseChanges.getLocation());
                    }
                }
            }
            if (booksFlag == null || booksFlag.equals("add")) {
                publishingHouse.getBooks().addAll(books);
            } else if (booksFlag.equals("upd")) {
                publishingHouse.setBooks(books);
            } else if (booksFlag.equals("del")) {
                publishingHouse.getBooks().removeAll(books);
            }
            return save(publishingHouse);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (InvalidFormatException ifException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ifException.getMessage());
        }

    }

    public ResponseEntity delete(Long Id) {
        ResponseEntity publishingHouseResponseEntity = getById(Id);
        if (publishingHouseResponseEntity.getStatusCode().is4xxClientError()) {
            return publishingHouseResponseEntity;
        } else {
            PublishingHouse publishingHouse = (PublishingHouse) publishingHouseResponseEntity.getBody();
            publishingHouseRepository.delete(publishingHouse);
            return ResponseEntity.ok().build();
        }
    }

}
