package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.NotFoundEntityException;
import models.Author;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.springframework.http.MediaType;
import services.interfaces.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            return ResponseEntity.ok(authorService.save(author));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }

    }
    @GetMapping(value = "/authors/{authorId}")
    public ResponseEntity getAuthorById(@PathVariable(value = "authorId") Long authorId) {
        try {
            Author author = authorService.getById(authorId);
            ObjectMapper mapper = new ObjectMapper();
            String authorJson = mapper.writeValueAsString(author);

            JSONObject jsonAuthor = new JSONObject(authorJson);
            jsonAuthor.put("booksCount", author.getBooks().size());

            return ResponseEntity.ok(jsonAuthor.toString());
        } catch (NotFoundEntityException | JsonProcessingException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }


    @PutMapping(value = "/authors/{authorId}")
    @ResponseBody
    public ResponseEntity changeAuthor(@PathVariable("authorId") Long authorId, @RequestParam MultiValueMap<String, String> params) {
        try {
            return ResponseEntity.ok(authorService.updateFromParams(authorId, params));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (NotFoundEntityException | NumberFormatException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

    @DeleteMapping(value = "/authors/{authorId}")
    public ResponseEntity removeAuthor(@PathVariable("authorId") Long Id) {
        try {
            authorService.delete(Id);
            return ResponseEntity.ok(authorService.getAll());
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }
}
