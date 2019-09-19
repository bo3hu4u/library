package lib_group.library.services.implementations;

import lib_group.library.repositories.AuthorRepository;
import lib_group.library.models.Author;
import lib_group.library.models.Book;
import lib_group.library.services.interfaces.IAuthorService;
import lib_group.library.services.interfaces.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

@Service("authorService")
public class AuthorService implements IAuthorService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private IBookService bookService;


    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public ResponseEntity getById(Long Id) {
        Author author = authorRepository.findById(Id).orElse(null);
        if (author == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't find author with id " + Id);
        }
        return ResponseEntity.ok(author);
    }

    public ResponseEntity findByName(String authorName) {
        if (authorName == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author's name is null");
        }
        Author authorByName = authorRepository.findAuthorByName(authorName);
        if (authorByName == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author with a name '" + authorName + "' not found");
        }
        return ResponseEntity.ok(authorByName);
    }

    public List<Author> saveAll(Set<Author> objects) {
        return authorRepository.saveAll(objects);
    }

    public ResponseEntity save(Author author) {
        Boolean newAuthor = (author.getAuthorId() == null);
        try {
            String resultBooksCheck = checkBooksExisting(author);
            if (resultBooksCheck != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultBooksCheck);
            }

            Set<Book> authorBooks = null;
            if (!newAuthor) {
                authorBooks = new HashSet<>(authorRepository.findById(author.getAuthorId()).get().getBooks());
                authorBooks.forEach(book -> book.setAuthor(null));
            } else {
                authorRepository.save(author);
                authorBooks = new HashSet<>();
            }
            for (Book currentBook : author.getBooks()) {
                currentBook.setAuthor(author);
                authorBooks.remove(currentBook);
                authorBooks.add(currentBook);
            }
            author.setBooks(authorBooks);

            authorRepository.save(author);
            return ResponseEntity.ok(author);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        }
    }

    private String checkBooksExisting(Author author) {
        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            Boolean anyBookHasNullId = author.getBooks().stream().anyMatch(book -> book.getBookId() == null);
            if (anyBookHasNullId) {
                Set<String> booksTitles = author.getBooks().stream().map(book -> book.getTitle()).collect(Collectors.toSet());
                Set<Book> foundBooksByTitle = bookService.findBooksByTitleIn(new ArrayList<>(booksTitles));
                if (foundBooksByTitle.size() < booksTitles.size()) {
                    booksTitles.removeAll(foundBooksByTitle.stream().map(book -> book.getTitle()).collect(Collectors.toList()));
                    return "These books not found: " + booksTitles;
                }
                author.setBooks(foundBooksByTitle);
            }
        } else {
            author.setBooks(new HashSet<>());
        }
        return null;
    }

    public ResponseEntity updateFromParams(Long Id, MultiValueMap<String, String> params) {
        ResponseEntity authorResponseEntity = getById(Id);
        if (authorResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return authorResponseEntity;
        }
        Author author = (Author) authorResponseEntity.getBody();

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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'" + parameterValues.get(0) + "' is incorrect value for 'birthYear'. It should be Integer");
                    }
                    break;
                case "books":
                    Set<Book> booksByTitle = bookService.findBooksByTitleIn(parameterValues);
                    author.getBooks().addAll(booksByTitle);
                    booksByTitle.forEach(book -> parameterValues.remove(book.getTitle()));
                    if (!parameterValues.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("These books not found: " + parameterValues);
                    }
                    break;
            }
        }
        return save(author);
    }

    public void deleteAll() {
        authorRepository.deleteAll();
    }

    public ResponseEntity delete(Long Id) {
        ResponseEntity authorResponseEntity = getById(Id);
        if (authorResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return authorResponseEntity;
        } else {
            Author author = (Author) authorResponseEntity.getBody();
            author.getBooks().forEach(book -> book.setAuthor(null));
            authorRepository.delete(author);
            return ResponseEntity.ok().build();
        }
    }

}
