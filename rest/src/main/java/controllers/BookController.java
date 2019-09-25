package controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.NotFoundEntityException;
import models.Book;
import services.interfaces.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class BookController {
    @Autowired
    private IBookService bookService;

    @GetMapping(value = "/books")
    public ResponseEntity getAllBooks() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @PostMapping(value = "/books")
    public ResponseEntity addNewBook(@RequestBody Book book) {
        try {
            return ResponseEntity.ok(bookService.save(book));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (NotFoundEntityException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping(value = "/books/{bookId}")
    public ResponseEntity getBookById(@PathVariable(value = "bookId") Long bookId) {

        try {
            return ResponseEntity.ok(bookService.getById(bookId));
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }


    }

    @PutMapping(value = "/books/{bookId}")
    @ResponseBody
    public ResponseEntity changeBook(@PathVariable("bookId") Long bookId, @Valid @RequestBody String json) throws IOException {
        try {
            return ResponseEntity.ok(bookService.updateFromJson(bookId, json));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getRootCause().getMessage());
        } catch (InvalidFormatException ifException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ifException.getMessage());
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }

    }

    @DeleteMapping(value = "/books/{bookId}")
    public ResponseEntity removeBook(@PathVariable("bookId") Long Id) {
        try {
            bookService.delete(Id);
            return ResponseEntity.ok(bookService.getAll());
        } catch (NotFoundEntityException exc) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
        }
    }

    @GetMapping(value = "/books/search")
    @ResponseBody
    public ResponseEntity getAuthorBooks(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(bookService.getAuthorBooks(name));
    }
}
