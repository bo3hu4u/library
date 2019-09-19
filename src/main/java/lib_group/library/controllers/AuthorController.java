package lib_group.library.controllers;

import lib_group.library.models.Author;
import lib_group.library.services.implementations.AuthorService;
import lib_group.library.services.interfaces.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthorController {
    @Autowired
    private IAuthorService authorService;

    @GetMapping(value = "/authors")
    public ResponseEntity getAllAuthors() {
        List<Author> authors = authorService.getAll();
        return ResponseEntity.ok(authors);
    }

    @PostMapping(value = "/authors")
    public ResponseEntity addNewAuthor(@RequestBody Author author) {
        return authorService.save(author);
    }

    @GetMapping(value = "/authors/{authorId}")
    public ResponseEntity getAuthorById(@PathVariable(value = "authorId") Long authorId) {
        return authorService.getById(authorId);
    }

    @PutMapping(value = "/authors/{authorId}")
    @ResponseBody
    public ResponseEntity changeAuthor(@PathVariable("authorId") Long authorId, @RequestParam MultiValueMap<String, String> params) {
        return authorService.updateFromParams(authorId, params);
    }

    @DeleteMapping(value = "/authors/{authorId}")
    public ResponseEntity removeAuthor(@PathVariable("authorId") Long Id) {
        ResponseEntity deletedAuthorResponseEntity = authorService.delete(Id);
        if (deletedAuthorResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return deletedAuthorResponseEntity;
        } else {
            return ResponseEntity.ok(authorService.getAll());
        }
    }
}
