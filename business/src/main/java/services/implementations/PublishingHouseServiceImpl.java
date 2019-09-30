<<<<<<< HEAD
package services.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.NotFoundEntityException;
import models.Location;
import models.Book;
import models.PublishingHouse;
import repositories.PublishingHouseRepository;
import services.implementations.common.CommonServiceImpl;
import services.interfaces.IBookService;
import services.interfaces.ILocationService;
import services.interfaces.IPublishingHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service("publishingHouseService")
public class PublishingHouseServiceImpl extends CommonServiceImpl<PublishingHouse, Long> implements IPublishingHouseService {
    @Autowired
    private PublishingHouseRepository publishingHouseRepository;
    @Autowired
    private IBookService bookService;
    @Autowired
    private ILocationService locationService;

    @Override
    public JpaRepository<PublishingHouse, Long> getRepository() {
        return publishingHouseRepository;
    }

    @Override
    public Class<PublishingHouse> getCurrentClass() {
        return PublishingHouse.class;
    }

    public PublishingHouse getByName(String name) {
        PublishingHouse publishingHouse = publishingHouseRepository.findPublishingHouseByName(name);
        if (publishingHouse == null) {
            throw new NotFoundEntityException("Can't find publishing house with name " + name);
        }
        return publishingHouse;
    }

    public Set<PublishingHouse> findAllByNameIn(List<String> names) {
        return publishingHouseRepository.findAllByNameIn(names);
    }

    @Override
    public PublishingHouse save(PublishingHouse publishingHouse) {

        if (publishingHouse.getLocation() != null) {
            Location location = locationService.getByAddress(publishingHouse.getLocation().getAddress());
            if (location != null) {
                setFreeOrOccupiedLocation(publishingHouse, location);
            } else {
                Location newLocation = new Location(publishingHouse.getLocation().getAddress());
                location = locationService.save(newLocation);
                publishingHouse.setLocation(location);
            }
        }

        if (publishingHouse.getBooks() != null && !publishingHouse.getBooks().isEmpty()) {
            Boolean anyBookHasNullId = publishingHouse.getBooks().stream().anyMatch(book -> book.getId() == null);
            if (anyBookHasNullId) {
                Set<String> booksTitles = publishingHouse.getBooks().stream().map(book -> book.getTitle()).collect(Collectors.toSet());
                Set<Book> foundBooksByTitle = bookService.findBooksByTitleIn(new ArrayList<>(booksTitles));
                publishingHouse.setBooks(foundBooksByTitle);
            }
        } else {
            publishingHouse.setBooks(new HashSet<>());
        }

        return publishingHouseRepository.save(publishingHouse);

    }

    private void setFreeOrOccupiedLocation(PublishingHouse publishingHouse, Location location) {
        if (publishingHouse.getId() == null || !(publishingHouse.equals(location.getPublishingHouse()))) {
            if (location.getPublishingHouse() == null) {
                publishingHouse.setLocation(location);
            } else {
                location.getPublishingHouse().setLocation(null);
                publishingHouseRepository.save(location.getPublishingHouse());
            }
        }
    }

    public PublishingHouse updateFromJson(Long Id, String jsonPublishingHouse, String booksFlag) throws IOException {

        PublishingHouse publishingHouse = getById(Id);
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
        if (booksFlag != null) {
            switch (booksFlag) {
                case "add":
                    publishingHouse.getBooks().addAll(books);
                    break;
                case "upd":
                    publishingHouse.setBooks(books);
                    break;
                case "del":
                    publishingHouse.getBooks().removeAll(books);
                    break;
                default:
                    break;
            }
        }
        return save(publishingHouse);
    }

    public void delete(Long Id) {
        PublishingHouse publishingHouse = getById(Id);
        publishingHouseRepository.delete(publishingHouse);
    }

}
=======
package services.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.NotFoundEntityException;
import models.Location;
import models.Book;
import models.PublishingHouse;
import repositories.PublishingHouseRepository;
import services.implementations.common.CommonServiceImpl;
import services.interfaces.IBookService;
import services.interfaces.ILocationService;
import services.interfaces.IPublishingHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("publishingHouseService")
public class PublishingHouseServiceImpl extends CommonServiceImpl<PublishingHouse, Long> implements IPublishingHouseService {
    @Autowired
    private PublishingHouseRepository publishingHouseRepository;
    @Autowired
    private IBookService bookService;
    @Autowired
    private ILocationService locationService;

    @Override
    public JpaRepository<PublishingHouse, Long> getRepository() {
        return publishingHouseRepository;
    }

    @Override
    public Class<PublishingHouse> getCurrentClass() {
        return PublishingHouse.class;
    }

    public PublishingHouse getByName(String name) {
        PublishingHouse publishingHouse = publishingHouseRepository.findPublishingHouseByName(name);
        if (publishingHouse == null) {
            throw new NotFoundEntityException("Can't find publishing house with name " + name);
        }
        return publishingHouse;
    }

    public Set<PublishingHouse> findAllByNameIn(List<String> names) {
        return publishingHouseRepository.findAllByNameIn(names);
    }

    @Override
    public PublishingHouse save(PublishingHouse publishingHouse) {

        if (publishingHouse.getLocation() != null) {
            Location location = locationService.getByAddress(publishingHouse.getLocation().getAddress());
            if (location != null) {
                setFreeOrOccupiedLocation(publishingHouse, location);
            } else {
                Location newLocation = new Location(publishingHouse.getLocation().getAddress());
                location = locationService.save(newLocation);
                publishingHouse.setLocation(location);
            }
        }

        return publishingHouseRepository.save(publishingHouse);

    }

    private void setFreeOrOccupiedLocation(PublishingHouse publishingHouse, Location location) {
        if (publishingHouse.getId() == null || !(publishingHouse.equals(location.getPublishingHouse()))) {
            if (location.getPublishingHouse() == null) {
                publishingHouse.setLocation(location);
            } else {
                location.getPublishingHouse().setLocation(null);
                publishingHouseRepository.save(location.getPublishingHouse());
            }
        }
    }

    public PublishingHouse updateFromJson(Long Id, String jsonPublishingHouse, String booksFlag) throws IOException {

        PublishingHouse publishingHouse = getById(Id);
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
        if (booksFlag != null) {
            switch (booksFlag) {
                case "add":
                    publishingHouse.getBooks().addAll(books);
                    break;
                case "upd":
                    publishingHouse.setBooks(books);
                    break;
                case "del":
                    publishingHouse.getBooks().removeAll(books);
                    break;
                default:
                    break;
            }
        }
        return save(publishingHouse);
    }

    public void delete(Long Id) {
        PublishingHouse publishingHouse = getById(Id);
        publishingHouseRepository.delete(publishingHouse);
    }

}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
