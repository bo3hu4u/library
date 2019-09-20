package lib_group.library.services.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lib_group.library.models.Author;
import lib_group.library.models.Book;
import lib_group.library.models.Description;
import lib_group.library.models.PublishingHouse;
import lib_group.library.repositories.BookRepository;
import lib_group.library.services.interfaces.IAuthorService;
import lib_group.library.services.interfaces.IBookService;
import lib_group.library.services.interfaces.IDescriptionService;
import lib_group.library.services.interfaces.IPublishingHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service("bookService")
public class BookService implements IBookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IPublishingHouseService publishingHouseService;
    @Autowired
    private IDescriptionService descriptionService;


    public List<Book> getAll() {
        List<Book> books = bookRepository.findAll();
        List<Description> descriptions = descriptionService.getAll();
        if (!descriptions.isEmpty() && !books.isEmpty()) {
            descriptions.forEach(description ->
                    books.stream().filter(book -> book.getDescriptionId() != null && book.getDescriptionId().equals(description.getId())).findFirst().get().setDescription(description)
            );
        }
        return books;
    }

    public Set<Book> getAuthorBooks(String authorName) {
        return bookRepository.findAllByAuthorName(authorName);
    }

    public ResponseEntity getById(Long Id) {
        Book book = bookRepository.findById(Id).orElse(null);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find book with id " + Id);
        }
        if (book.getDescriptionId() != null) {
            book.setDescription(descriptionService.findById(book.getDescriptionId()));
        }
        return ResponseEntity.ok(book);
    }

    public Set<Book> findBooksByTitleIn(List<String> titles) {
        return bookRepository.findBooksByTitleIn(titles);
    }

    public List<Book> saveAll(Set<Book> books) {
        return bookRepository.saveAll(books);
    }

    public ResponseEntity save(Book book) {
        editBookDescription(book);

        Boolean bookHasAuthorWithoutId = book.getAuthor() != null && book.getAuthor().getAuthorId() == null;
        if (bookHasAuthorWithoutId) {
            ResponseEntity authorResponseEntity = authorService.findByName(book.getAuthor().getName());
            if (authorResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return authorResponseEntity;
            } else {
                book.setAuthor((Author) authorResponseEntity.getBody());
            }
        }
        try {
            Set<PublishingHouse> publishingHouses = new HashSet<>(book.getPublishingHouses());
            bookRepository.save(book);

            String resultPublishingHousesCheck = checkPublishingHousesExisting(book, publishingHouses);
            if (resultPublishingHousesCheck != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultPublishingHousesCheck);
            }
            Set<PublishingHouse> bookPublishingHouses = bookRepository.findById(book.getBookId()).get().getPublishingHouses();
            bookPublishingHouses.forEach(pH -> pH.getBooks().remove(book));

            for (PublishingHouse currentPublishingHouse : book.getPublishingHouses()) {
                currentPublishingHouse.getBooks().add(book);
                bookPublishingHouses.remove(currentPublishingHouse);
                bookPublishingHouses.add(currentPublishingHouse);
            }
            publishingHouseService.saveAll(bookPublishingHouses);
            return ResponseEntity.ok(book);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        }
    }

    private void editBookDescription(Book book) {
        Boolean isNewDescription = book.getDescriptionId() == null && book.getDescription() != null;
        if (isNewDescription) {
            Description newDescription = descriptionService.saveDesc(book.getDescription());
            book.setDescriptionId(newDescription.getId());
        } else {
            if (book.getDescription() == null) {
                descriptionService.delete(book.getDescriptionId());
                book.setDescriptionId(null);
            } else {
                book.getDescription().setBookTitle(book.getTitle());
                descriptionService.saveDesc(book.getDescription());
            }
        }
    }

    private String checkPublishingHousesExisting(Book book, Set<PublishingHouse> publishingHouses) {
        if (publishingHouses != null && !publishingHouses.isEmpty()) {
            Boolean anyPublishingHouseHasIdNull = publishingHouses.stream().anyMatch(pH -> pH.getPublishHouseId() == null);
            if (anyPublishingHouseHasIdNull) {
                Set<String> publishingHousesNames = publishingHouses.stream().map(pH -> pH.getName()).collect(Collectors.toSet());
                Set<PublishingHouse> foundPublishingHousesNameIn = publishingHouseService.findAllByNameIn(new ArrayList<>(publishingHousesNames));
                if (foundPublishingHousesNameIn.size() < publishingHousesNames.size()) {
                    publishingHousesNames.removeAll(foundPublishingHousesNameIn.stream().map(pH -> pH.getName()).collect(Collectors.toList()));
                    return "These publishing houses not found: " + publishingHousesNames;
                }
                book.setPublishingHouses(foundPublishingHousesNameIn);
            }
        }
        return null;
    }

    public ResponseEntity updateFromJson(Long bookId, String jsonBook) throws IOException {

        ResponseEntity bookResponseEntity = getById(bookId);
        if (bookResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return bookResponseEntity;
        }
        Book currentBook = (Book) bookResponseEntity.getBody();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(jsonBook, new TypeReference<Map<String, Object>>() {
            });
            Book bookChanges = mapper.readValue(jsonBook, Book.class);
            LinkedHashMap<String, Object> authorJsonMap = (LinkedHashMap<String, Object>) map.get("author");

            if (map.size() != 0) {
                Author authorChanges = null;
                if (authorJsonMap == null && map.containsKey("author")) {
                    currentBook.setAuthor(null);
                } else if (authorJsonMap != null) {
                    ResponseEntity authorResponseEntity = authorService.findByName(bookChanges.getAuthor().getName());
                    if (authorResponseEntity.getBody() instanceof Author) {
                        authorChanges = (Author) authorResponseEntity.getBody();
                    }
                    if (authorChanges == null) {
                        authorChanges = currentBook.getAuthor();
                    } else {
                        if (authorJsonMap.containsKey("name")) {
                            authorChanges.setName((String) authorJsonMap.get("name"));
                        }
                        if (authorJsonMap.containsKey("birthYear")) {
                            authorChanges.setBirthYear((Integer) authorJsonMap.get("birthYear"));
                        }
                    }
                    currentBook.setAuthor(authorChanges);
                }

                if (map.containsKey("title")) {
                    currentBook.setTitle(bookChanges.getTitle());
                }
                if (map.containsKey("editionYear")) {
                    currentBook.setEditionYear(bookChanges.getEditionYear());
                }
                if (map.containsKey("bookOnHands")) {
                    currentBook.setBookOnHands(bookChanges.isBookOnHands());
                }
                if (map.containsKey("publishingHouses")) {
                    currentBook.setPublishingHouses(bookChanges.getPublishingHouses());
                }
                bookResponseEntity = save(currentBook);
            }
            return bookResponseEntity;
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (InvalidFormatException ifException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ifException.getMessage());
        }
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }

    public ResponseEntity delete(Long Id) {
        ResponseEntity deletedBookResponseEntity = getById(Id);
        if (deletedBookResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return deletedBookResponseEntity;
        } else {
            Book book = (Book) deletedBookResponseEntity.getBody();
            book.getPublishingHouses().forEach(pH -> pH.getBooks().remove(book));
            publishingHouseService.saveAll(book.getPublishingHouses());
            descriptionService.delete(book.getDescriptionId());
            bookRepository.delete(book);
            return ResponseEntity.ok().build();
        }
    }
}
