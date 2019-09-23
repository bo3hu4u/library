package lib_group.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lib_group.library.models.*;
import lib_group.library.services.*;
import lib_group.library.controllers.AuthorControllerTest;
import lib_group.library.controllers.BookControllerTest;
import lib_group.library.controllers.PublishingHouseControllerTest;
import lib_group.library.services.implementations.*;
import lib_group.library.services.interfaces.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@RunWith(Suite.class)
@SpringBootTest(classes = LibraryApplication.class)
@WebAppConfiguration
@SuiteClasses({AuthorControllerTest.class, BookControllerTest.class, LocationServiceTest.class, PublishingHouseControllerTest.class})
public class LibraryApplicationTests {
    @Autowired
    public IAuthorService authorService;
    @Autowired
    public IBookService bookService;
    @Autowired
    public ILocationService locationService;
    @Autowired
    public IPublishingHouseService publishingHouseService;
    @Autowired
    public IDescriptionService descriptionService;
    @Autowired
    public WebApplicationContext wac;

    public MockMvc mockMvc;

    @Autowired
    public ResetAutoIncrementComponent resetAutoIncrementComponent;

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Before
    public void startTest() {
        resetAutoIncrementComponent.autoIncrementTo1();
        descriptionService.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        bookService.save(new Book("book1", 2000, false));
        bookService.save(new Book("book2", 2010, true));
        bookService.save(new Book("book3", 2050, false));
        Book book4 = new Book("book4", 2070, true);
        book4.setDescription(new Description("book4", "It's description for book4"));
        bookService.save(book4);
        System.out.println("All books added");

        authorService.save(new Author("author1", 111));
        authorService.save(new Author("author2", 222));
        authorService.save(new Author("author3", 333));
        authorService.save(new Author("author4", 444));
        System.out.println("All authors added");


        publishingHouseService.save(new PublishingHouse("Publish1", new Location("address1")));
        publishingHouseService.save(new PublishingHouse("Publish2", new Location("address2")));
        publishingHouseService.save(new PublishingHouse("Publish3", new Location("address3")));
        publishingHouseService.save(new PublishingHouse("Publish4", new Location("address4")));

        System.out.println("All publishing houses added");

        Author author =  authorService.findByName("author1");
        Set<Book> books = bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book1", "book2"}));

        for (Book book : books) {
            book.setAuthor(author);
        }
        bookService.saveAll(books);

        author =  authorService.findByName("author2");
        books = bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book3"}));
        for (Book book : books) {
            book.setAuthor(author);
        }
        bookService.saveAll(books);

        author =  authorService.findByName("author3");
        books = bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book4"}));
        for (Book book : books) {
            book.setAuthor(author);
        }
        bookService.saveAll(books);

        PublishingHouse ph =  publishingHouseService.getByName("Publish1");
        ph.setBooks(bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book1", "book2"})));
        publishingHouseService.save(ph);
        ph =  publishingHouseService.getByName("Publish2");
        ph.setBooks(bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book1", "book3"})));
        publishingHouseService.save(ph);
        ph =  publishingHouseService.getByName("Publish3");
        ph.setBooks(bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book3"})));
        publishingHouseService.save(ph);


        publishingHouseService.save(new PublishingHouse("Publish5"));
        locationService.save(new Location("address5"));
    }


    @After
    public void finishTest() {
        publishingHouseService.deleteAll();
        System.out.println("All publish houses deleted");
        locationService.deleteAll();
        System.out.println("All addresses deleted");

        bookService.deleteAll();
        System.out.println("All books deleted");
        authorService.deleteAll();
        // authorService.getAll().forEach(act -> authorService.delete(act.getId()));
        System.out.println("All authors deleted");
        descriptionService.deleteAll();
    }

}
