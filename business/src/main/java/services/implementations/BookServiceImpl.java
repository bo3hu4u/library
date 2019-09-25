package services.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.NotFoundEntityException;
import models.Author;
import models.Book;
import models.Description;
import models.PublishingHouse;
import repositories.BookRepository;
import services.implementations.common.CommonServiceImpl;
import services.interfaces.IAuthorService;
import services.interfaces.IBookService;
import services.interfaces.IDescriptionService;
import services.interfaces.IPublishingHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service("bookService")
public class BookServiceImpl extends CommonServiceImpl<Book, Long> implements IBookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IPublishingHouseService publishingHouseService;
    @Autowired
    private IDescriptionService descriptionService;

    @Override
    public List<Book> getAll() {
        List<Book> books = bookRepository.findAll();
        List<Description> descriptions = descriptionService.getAll();

        if (!descriptions.isEmpty() && !books.isEmpty()) {
            descriptions.forEach(description -> {
                        books.stream().filter(book -> book.getId().equals(description.getBookId())).findFirst().get().setDescription(description);
                    }
            );
        }

        return books;
    }

    public Set<Book> getAuthorBooks(String authorName) {
        return bookRepository.findAllByAuthorName(authorName);
    }

    @Override
    public JpaRepository<Book, Long> getRepository() {
        return bookRepository;
    }

    @Override
    public Class<Book> getCurrentClass() {
        return Book.class;
    }

    @Override
    public Book getById(Long Id) {
        Book book = super.getById(Id);
        book.setDescription(descriptionService.getByBookId(Id));
        return book;
    }

    public Set<Book> findBooksByTitleIn(List<String> titles) {
        return bookRepository.findBooksByTitleIn(titles);
    }

    @Override
    public Book save(Book book) {
        Boolean authorWithoutId = book.getAuthor() != null && book.getAuthor().getId() == null;
        if (authorWithoutId) {
            book.setAuthor(authorService.findByName(book.getAuthor().getName()));
        }
        Set<PublishingHouse> publishingHouses = new HashSet<>(book.getPublishingHouses());

        bookRepository.save(book);
        setBookDescription(book);
        setBookToPublishingHouses(book, publishingHouses);
        return book;
    }


    private void setBookDescription(Book book) {
        Description description = descriptionService.getByBookId(book.getId());

        if (description == null && book.getDescription() != null) {
            Description newDescription = book.getDescription();
            newDescription.setBookId(book.getId());
            descriptionService.save(book.getDescription());
        }
        if (description != null) {
            if (book.getDescription() == null) {
                descriptionService.delete(description.getId());
            } else {
                descriptionService.save(book.getDescription());
            }
        }

    }

    private void setBookToPublishingHouses(Book book, Set<PublishingHouse> publishingHouses) throws
            NotFoundEntityException {
        if (publishingHouses != null && !publishingHouses.isEmpty()) {
            Boolean anyPublishingHouseHasIdNull = publishingHouses.stream().anyMatch(pH -> pH.getId() == null);
            if (anyPublishingHouseHasIdNull) {
                Set<String> publishingHousesNames = publishingHouses.stream().map(pH -> pH.getName()).collect(Collectors.toSet());
                Set<PublishingHouse> foundPublishingHousesNameIn = publishingHouseService.findAllByNameIn(new ArrayList<>(publishingHousesNames));
                if (foundPublishingHousesNameIn.size() < publishingHousesNames.size()) {
                    publishingHousesNames.removeAll(foundPublishingHousesNameIn.stream().map(pH -> pH.getName()).collect(Collectors.toList()));
                    throw new NotFoundEntityException("These publishing houses not found: " + publishingHousesNames);
                }
                book.setPublishingHouses(foundPublishingHousesNameIn);
            }
        }
        Set<PublishingHouse> bookPublishingHouses = bookRepository.findById(book.getId()).get().getPublishingHouses();
        bookPublishingHouses.forEach(pH -> pH.getBooks().remove(book));
        Set<PublishingHouse> nowPublishingHouses = new HashSet<>(book.getPublishingHouses());
        for (PublishingHouse currentPublishingHouse : nowPublishingHouses) {
            currentPublishingHouse.getBooks().add(book);
            bookPublishingHouses.remove(currentPublishingHouse);
            bookPublishingHouses.add(currentPublishingHouse);
        }
        publishingHouseService.saveAll(bookPublishingHouses);
    }

    public Book updateFromJson(Long bookId, String jsonBook) throws IOException {
        Book currentBook = getById(bookId);
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

                try {
                    authorChanges = authorService.findByName(bookChanges.getAuthor().getName());
                } catch (NotFoundEntityException exc) {
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
            save(currentBook);
        }
        return currentBook;

    }

    @Transactional
    public void delete(Long Id) throws NotFoundEntityException {
        Book book = getById(Id);
        book.getPublishingHouses().forEach(pH -> pH.getBooks().remove(book));

        descriptionService.deleteByBookId(Id);
        bookRepository.delete(book);
    }
}
