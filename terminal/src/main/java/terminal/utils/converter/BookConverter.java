package terminal.utils.converter;

import models.Book;
import org.springframework.stereotype.Component;
import terminal.utils.converter.common.BaseObjectConverter;

@Component
public class BookConverter extends BaseObjectConverter<Book> {
    @Override
    public Class<Book> getThisClass() {
        return Book.class;
    }
}