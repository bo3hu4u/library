<<<<<<< HEAD
package services.interfaces;

import models.Book;
import services.interfaces.common.ICommonService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IBookService extends ICommonService<Book,Long> {
    Set<Book> findBooksByTitleIn(List<String> titles);

    Set<Book> getAuthorBooks(String authorName);

    Book updateFromJson(Long bookId, String json) throws IOException;
}
=======
package services.interfaces;

import models.Book;
import services.interfaces.common.ICommonService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IBookService extends ICommonService<Book,Long> {
    Set<Book> findBooksByTitleIn(List<String> titles);

    Set<Book> getAuthorBooks(String authorName);

    Book updateFromJson(Long bookId, String json) throws IOException;
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
