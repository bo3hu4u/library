package lib_group.library.repositories;

import lib_group.library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Set<Book> findAllByAuthorName(String authorName);
    Set<Book> findBooksByTitleIn(Collection<String> titles);
}

