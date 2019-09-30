package terminal.utils.converter;

import models.Author;
import org.springframework.stereotype.Component;
import terminal.utils.converter.common.BaseObjectConverter;

@Component
public class AuthorConverter extends BaseObjectConverter<Author> {
    @Override
    public Class<Author> getThisClass() {
        return Author.class;
    }
}
