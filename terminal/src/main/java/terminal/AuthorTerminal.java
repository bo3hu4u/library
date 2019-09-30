package terminal;

import exceptions.NotFoundEntityException;
import models.Author;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import services.interfaces.IAuthorService;
import terminal.utils.printer.AuthorTablePrinter;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class AuthorTerminal {

    @Autowired
    @Lazy
    private Terminal terminal;
    @Autowired
    @Lazy
    private LineReader lineReader;
    @Autowired
    private IAuthorService authorService;

    @Autowired
    private AuthorTablePrinter authorTablePrinter;


    @ShellMethod("Get all authors")
    public void getAllAuthors() {
        List<Author> authors = authorService.getAll();
        authorTablePrinter.printAuthorsTable(authors);
    }

    @ShellMethod("Add new author")
    public void addAuthor(Author author) {
        try {
            if (author != null) {
                authorService.save(author);

                List<Author> authorList = new ArrayList<>();
                authorList.add(author);
                terminal.writer().println();

                authorTablePrinter.printAuthorsTable(authorList);
            }
        } catch (DataIntegrityViolationException e) {
            terminal.writer().println(e.getRootCause().getMessage());
        } catch (NotFoundEntityException exc) {
            terminal.writer().println(exc.getMessage());
        }

    }


    @ShellMethod("get author by ID")
    public void getAuthorById(Long Id) {
        try {
            List<Author> authorList = new ArrayList<>();
            authorList.add(authorService.getById(Id));
            authorTablePrinter.printAuthorsTable(authorList);
        } catch (NotFoundEntityException exc) {
            terminal.writer().println(exc.getMessage());
        }


    }

    @ShellMethod("Delete author")
    public void deleteAuthor(Long Id) {
        try {
            while (true) {
                terminal.writer().println("delete author with id: " + Id + "? Y/N ");
                String answer = lineReader.readLine();
                if (answer.equalsIgnoreCase("Y")) {
                    authorService.delete(Id);
                    terminal.writer().println("author deleted");
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

    @ShellMethod("Update author")
    public void updateAuthor(Long Id) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            String name = lineReader.readLine("Name: ");
            if (StringUtils.hasText(name)) {
                params.add("name", name);
            }
            String birthYear = lineReader.readLine("birthYear: ");
            if (StringUtils.hasText(birthYear)) {
                params.add("birthYear", birthYear);
            }

            int index = 1;
            while (true) {
                String book = lineReader.readLine(index + ". book: ");
                if (book.endsWith("exit;")) {
                    break;
                }
                if (StringUtils.hasText(book)) {
                    if (!params.containsKey("books")) {
                        params.add("books", book);
                    } else {
                        params.get("books").add(book);
                    }
                    index++;
                }
            }

            Author author = authorService.updateFromParams(Id, params);
            List<Author> authorList = new ArrayList<>();
            authorList.add(author);

            authorTablePrinter.printAuthorsTable(authorList);

        } catch (DataIntegrityViolationException e) {
            terminal.writer().println(e.getRootCause().getMessage());
        } catch (NotFoundEntityException | NumberFormatException exc) {
            terminal.writer().println(exc.getMessage());
        }


    }

}
