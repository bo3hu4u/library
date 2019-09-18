package lib_group.library;

import lib_group.library.models.Author;
import lib_group.library.models.Book;
import lib_group.library.models.PublishingHouse;
import lib_group.library.ui.editors.PublishingHouseListEditor;
import lib_group.library.ui.views.author.AuthorDialogView;
import lib_group.library.ui.editors.BookListEditor;
import lib_group.library.ui.views.book.BookDialogView;
import lib_group.library.ui.views.publishing_house.PublishingHouseDialogView;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
public class LibraryConfig {


    @Bean
    public Function<Book, BookDialogView> bookDialogViewFactory() {
        return book -> newBookViewDialog(book);
    }

    @Bean
    @Scope(value = "prototype")
    public BookDialogView newBookViewDialog(Book book) {
        return new BookDialogView(book);
    }

    @Bean
    public Function<Author, AuthorDialogView> authorDialogViewFactory() {
        return author -> newAuthorDialogView(author);
    }

    @Bean
    @Scope(value = "prototype")
    public AuthorDialogView newAuthorDialogView(Author author) {
        return new AuthorDialogView(author);
    }

    @Bean
    public Function<PublishingHouse, PublishingHouseDialogView> publishingHouseDialogView() {
        return publishingHouse -> newPublishingHouseDialogView(publishingHouse);
    }

    @Bean
    @Scope(value = "prototype")
    public PublishingHouseDialogView newPublishingHouseDialogView(PublishingHouse publishingHouse) {
        return new PublishingHouseDialogView(publishingHouse);
    }

    @Bean
    public Function<List<Book>, BookListEditor> bookListEditorFunction() {
        return books -> newBookListEditor(books);
    }

    @Bean
    @Scope(value = "prototype")
    public BookListEditor newBookListEditor(List<Book> books) {
        return new BookListEditor(books);
    }


    @Bean
    public Function<List<PublishingHouse>, PublishingHouseListEditor> publishingHouseListEditorFunction() {
        return publishingHouses -> newPublishingHouseListEditor(publishingHouses);
    }

    @Bean
    @Scope(value = "prototype")
    public PublishingHouseListEditor newPublishingHouseListEditor(List<PublishingHouse> publishingHouses) {
        return new PublishingHouseListEditor(publishingHouses);
    }


}
