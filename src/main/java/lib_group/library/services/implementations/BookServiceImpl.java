package lib_group.library.services.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lib_group.library.exceptions.NotFoundEntityException;
import lib_group.library.models.Author;
import lib_group.library.models.Book;
import lib_group.library.models.Description;
import lib_group.library.models.PublishingHouse;
import lib_group.library.repositories.BookRepository;
import lib_group.library.services.implementations.common.CommonServiceImpl;
import lib_group.library.services.interfaces.IAuthorService;
import lib_group.library.services.interfaces.IBookService;
import lib_group.library.services.interfaces.IDescriptionService;
import lib_group.library.services.interfaces.IPublishingHouseService;
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
            descriptions.forEach(description ->
                    books.stream().filter(book -> book.getDescriptionId() != null && book.getDescriptionId().equals(description.getId())).findFirst().get().setDescription(description)
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
        if (book.getDescriptionId() != null) {
            book.setDescription(descriptionService.getById(book.getDescriptionId()));
        }
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
        setBookDescription(book);
        bookRepository.save(book);

        setBookToPublishingHouses(book, publishingHouses);
        return book;
    }


    private void setBookDescription(Book book) {
        Boolean isNewDescription = book.getDescriptionId() == null && book.getDescription() != null;
        if (isNewDescription) {
            Description newDescription = descriptionService.save(book.getDescription());
            book.setDescriptionId(newDescription.getId());
        } else {
            if (book.getDescription() == null) {
                descriptionService.delete(book.getDescriptionId());
                book.setDescriptionId(null);
            } else {
                book.getDescription().setBookTitle(book.getTitle());
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
        descriptionService.delete(book.getDescriptionId());
        bookRepository.delete(book);
    }
}
