package terminal;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.NotFoundEntityException;
import models.Book;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import services.interfaces.IBookService;
import terminal.utils.printer.BookTablePrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class BookTerminal {
    @Autowired
    @Lazy
    private Terminal terminal;
    @Autowired
    @Lazy
    private LineReader lineReader;
    @Autowired
    private IBookService bookService;
    @Autowired
    private BookTablePrinter bookTablePrinter;

    @ShellMethod("Get all books")
    public void getAllBooks() {
        List<Book> books = bookService.getAll();
        bookTablePrinter.printBooksTable(books);
    }

    @ShellMethod("get book by ID")
    public void getBookById(Long Id) {
        try {
            List<Book> bookList = new ArrayList<>();
            bookList.add(bookService.getById(Id));
            bookTablePrinter.printBooksTable(bookList);
        } catch (NotFoundEntityException exc) {
            terminal.writer().println(exc.getMessage());
        }
    }

    @ShellMethod("Add new book")
    public void addBook(Book book) {
        try {
            if (book != null) {
                bookService.save(book);
                List<Book> bookList = new ArrayList<>();
                bookList.add(book);
                bookTablePrinter.printBooksTable(bookList);
            }
        } catch (DataIntegrityViolationException e) {
            terminal.writer().println(e.getRootCause().getMessage());
        } catch (NotFoundEntityException exc) {
            terminal.writer().println(exc.getMessage());
        }
    }

    @ShellMethod("Update book")
    public void updateBook(Long Id, String json) throws IOException {
        try {
            Book book = bookService.updateFromJson(Id, json);
            List<Book> bookList = new ArrayList<>();
            bookList.add(book);
            bookTablePrinter.printBooksTable(bookList);
        } catch (DataIntegrityViolationException e) {
            terminal.writer().println(e.getRootCause().getMessage());
        } catch (InvalidFormatException | NotFoundEntityException ifException) {
            terminal.writer().println(ifException.getMessage());
        }

    }

    @ShellMethod("delete book")
    public void deleteBook(Long Id) {
        try {
            while (true) {
                terminal.writer().println("delete book with id: " + Id + "? Y/N ");
                String answer = lineReader.readLine();
                if (answer.equalsIgnoreCase("Y")) {
                    bookService.delete(Id);
                    terminal.writer().println("book deleted");
                    break;
                }
                if (answer.equalsIgnoreCase("N")) {
                    terminal.writer().println("delete action canceled");
                    break;
                }
            }
        } catch (NotFoundEntityException exc) {
            terminal.writer().println(exc.getMessage());
        }
    }

    @ShellMethod("get books by author name")
    public void getAuthorBooks(String name) {
        try {
            bookTablePrinter.printBooksTable(bookService.getAuthorBooks(name).stream().collect(Collectors.toList()));
        } catch (NotFoundEntityException exception) {
            terminal.writer().println(exception.getMessage());
        }
    }


}
