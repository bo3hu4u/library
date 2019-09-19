package lib_group.library.controllers;

import lib_group.library.models.Book;
import lib_group.library.services.interfaces.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return bookService.save(book);
    }

    @GetMapping(value = "/books/{bookId}")
    public ResponseEntity getBookById(@PathVariable(value = "bookId") Long bookId) {
        return bookService.getById(bookId);
    }

    @PutMapping(value = "/books/{bookId}")
    @ResponseBody
    public ResponseEntity changeBook(@PathVariable("bookId") Long bookId, @Valid @RequestBody String json) throws IOException {
        return bookService.updateFromJson(bookId, json);
    }

    @DeleteMapping(value = "/books/{bookId}")
    public ResponseEntity removeBook(@PathVariable("bookId") Long Id) {
        ResponseEntity deletedBookResponseEntity = bookService.delete(Id);
        if (deletedBookResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return deletedBookResponseEntity;
        } else {
            return ResponseEntity.ok(bookService.getAll());
        }
    }

    @GetMapping(value = "/books/search")
    @ResponseBody
    public ResponseEntity getAuthorBooks(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(bookService.getAuthorBooks(name));
    }
}
