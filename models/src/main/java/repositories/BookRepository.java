<<<<<<< HEAD
package repositories;

import models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Set<Book> findAllByAuthorName(String authorName);

    Set<Book> findBooksByTitleIn(Collection<String> titles);
}

=======
package repositories;

import models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Set<Book> findAllByAuthorName(String authorName);

    Set<Book> findBooksByTitleIn(Collection<String> titles);
}

>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
