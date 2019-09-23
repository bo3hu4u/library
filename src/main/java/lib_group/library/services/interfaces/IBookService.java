package lib_group.library.services.interfaces;

import lib_group.library.models.Book;
import lib_group.library.services.interfaces.common.ICommonService;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IBookService extends ICommonService<Book,Long> {
    Set<Book> findBooksByTitleIn(List<String> titles);

    Set<Book> getAuthorBooks(String authorName);

    Book updateFromJson(Long bookId, String json) throws IOException;
}
