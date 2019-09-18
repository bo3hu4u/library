package lib_group.library;


import lib_group.library.models.*;
import lib_group.library.services.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
public class LibraryApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LibraryApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    //@Bean
    public int demoData(AuthorService authorService, BookService bookService, PublishingHouseService publishingHouseService, LocationService locationService, DescriptionService descriptionService) {
        descriptionService.deleteAll();
        bookService.save(new Book("book1", 2000, false));
        bookService.save(new Book("book2", 2010, true));
        bookService.save(new Book("book3", 2050, false));
        Book book4 = new Book("book4", 2070, true);
        book4.setDescObj(new Description("book4", "It's description for book4"));
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

        Author author = (Author) authorService.findByName("author1").getBody();
        Set<Book> books = bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book1", "book2"}));

        for (Book book : books) {
            book.setAuthor(author);
        }
        bookService.saveAll(books);

        author = (Author) authorService.findByName("author2").getBody();
        books = bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book3"}));
        for (Book book : books) {
            book.setAuthor(author);
        }
        bookService.saveAll(books);

        author = (Author) authorService.findByName("author3").getBody();
        books = bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book4"}));
        for (Book book : books) {
            book.setAuthor(author);
        }
        bookService.saveAll(books);

        PublishingHouse ph = (PublishingHouse) publishingHouseService.getByName("Publish1").getBody();
        ph.setBooks(bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book1", "book2"})));
        publishingHouseService.save(ph);
        ph = (PublishingHouse) publishingHouseService.getByName("Publish2").getBody();
        ph.setBooks(bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book1", "book3"})));
        publishingHouseService.save(ph);
        ph = (PublishingHouse) publishingHouseService.getByName("Publish3").getBody();
        ph.setBooks(bookService.findBooksByTitleIn(Arrays.asList(new String[]{"book3"})));
        publishingHouseService.save(ph);

        publishingHouseService.save(new PublishingHouse("Publish5"));
        locationService.save(new Location("address5"));

        Book bookForChange = (Book) bookService.getById(4L).getBody();

        bookForChange.getDescObj().setBookDescription("It's UPDATED description for book4 It's UPDATED description for book4 It's UPDATED description for book4" +
                "It's UPDATED description for book4 It's UPDATED description for book4");
        bookService.save(bookForChange);

        return 0;
    }

}
