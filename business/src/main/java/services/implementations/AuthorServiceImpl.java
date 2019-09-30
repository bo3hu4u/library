package services.implementations;

import exceptions.NotFoundEntityException;
import repositories.AuthorRepository;
import models.Author;
import models.Book;
import services.implementations.common.CommonServiceImpl;
import services.interfaces.IAuthorService;
import services.interfaces.IBookService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service("authorService")
public class AuthorServiceImpl extends CommonServiceImpl<Author, Long> implements IAuthorService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private IBookService bookService;

    @PersistenceContext
    private EntityManager em;

    @Override
    public JpaRepository<Author, Long> getRepository() {
        return authorRepository;
    }

    @Override
    public Class<Author> getCurrentClass() {
        return Author.class;
    }

    public Author findByName(String authorName) {
        if (authorName == null) {
            throw new NotFoundEntityException("Author's name is null");
        }
        Author authorByName = authorRepository.findAuthorByName(authorName);
        if (authorByName == null) {
            throw new NotFoundEntityException("Author with a name '" + authorName + "' not found");
        }
        return authorByName;
    }

    @Override
    public Author save(Author author) {
        checkBooksExisting(author);
        authorRepository.save(author);
        setAuthorToBooks(author);
        return author;
    }


    private void setAuthorToBooks(Author author) {
        Set<Book> authorBooks = null;
        Session session = (Session) em.getDelegate();
        if (session.isOpen()) {
            session.evict(author);
        }

        if (author.getId() != null) {
            authorBooks = new HashSet<>(authorRepository.findById(author.getId()).get().getBooks());
            authorBooks.forEach(book -> book.setAuthor(null));
        } else {
            authorBooks = new HashSet<>();
        }


        for (Book currentBook : author.getBooks()) {
            currentBook.setAuthor(author);
            authorBooks.remove(currentBook);
            authorBooks.add(currentBook);
        }
        bookService.saveAll(authorBooks);
    }

    private void checkBooksExisting(Author author) {
        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            Boolean anyBookHasNullId = author.getBooks().stream().anyMatch(book -> book.getId() == null);
            if (anyBookHasNullId) {
                Set<String> booksTitles = author.getBooks().stream().map(book -> book.getTitle()).collect(Collectors.toSet());
                Set<Book> foundBooksByTitle = bookService.findBooksByTitleIn(new ArrayList<>(booksTitles));
                author.setBooks(foundBooksByTitle);
            }
        } else {
            author.setBooks(new HashSet<>());
        }
    }

    public Author updateFromParams(Long Id, MultiValueMap<String, String> params) {
        Author author = getById(Id);
        Set<Map.Entry<String, List<String>>> paramsSet = params.entrySet();
        for (Map.Entry<String, List<String>> parameter : paramsSet) {
            List<String> parameterValues = parameter.getValue();
            switch (parameter.getKey()) {
                case "name":
                    if (!parameter.getValue().get(0).equals("")) {
                        author.setName(parameterValues.get(0));
                    }
                    break;
                case "birthYear":
                    try {
                        author.setBirthYear(Integer.valueOf(parameterValues.get(0)));
                    } catch (Exception e) {
                        throw new NumberFormatException("'" + parameterValues.get(0) + "' is incorrect value for 'birthYear'. It should be Integer");
                    }
                    break;
                case "books":
                    Set<Book> booksByTitle = parameterValues.stream().map(title -> new Book(title)).collect(Collectors.toSet());
                    author.setBooks(booksByTitle);
                    break;
            }
        }
        return save(author);
    }

    @Transactional
    public void delete(Long Id) {
        Author author = getById(Id);
        author.getBooks().forEach(book -> book.setAuthor(null));
        authorRepository.delete(author);
    }

}
