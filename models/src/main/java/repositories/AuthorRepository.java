<<<<<<< HEAD
package repositories;

import models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findAuthorByName(String authorName);
}

=======
package repositories;

import models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findAuthorByName(String authorName);
}

>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
