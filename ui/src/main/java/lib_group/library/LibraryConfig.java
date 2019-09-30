package lib_group.library;

import models.Book;
import models.PublishingHouse;
import lib_group.library.ui.editors.BookListEditor;
import lib_group.library.ui.editors.PublishingHouseListEditor;
import lib_group.library.ui.views.factories.ViewDialogFactory;
import lib_group.library.ui.views.author.AuthorDialogView;
import lib_group.library.ui.views.book.BookDialogView;
import lib_group.library.ui.views.publishing_house.PublishingHouseDialogView;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
public class LibraryConfig {

    @Bean
    public ServiceLocatorFactoryBean serviceLocatorBean() {
        ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
        bean.setServiceLocatorInterface(ViewDialogFactory.class);
        return bean;
    }

    @Bean("publishingHouseDialog")
    @Scope(value = "prototype")
    public PublishingHouseDialogView newPublishingHouseDialogView() {
        return new PublishingHouseDialogView();
    }

    @Bean("bookDialog")
    @Scope(value = "prototype")
    public BookDialogView newBookViewDialog() {
        return new BookDialogView();
    }


    @Bean("authorDialog")
    @Scope(value = "prototype")
    public AuthorDialogView newAuthorDialogView() {
        return new AuthorDialogView();
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
