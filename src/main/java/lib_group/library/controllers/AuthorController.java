package lib_group.library.controllers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import elemental.json.JsonObject;
import lib_group.library.exceptions.NotFoundEntityException;
import lib_group.library.models.Author;
import lib_group.library.services.interfaces.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

            return ResponseEntity.ok(author);
        } catch (NotFoundEntityException exc) {
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
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        } catch (NumberFormatException exc) {
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
