package terminal.utils.printer;

import models.Author;
import models.Book;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.table.*;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthorTablePrinter {

    @Autowired
    @Lazy
    private Terminal terminal;
    private LinkedHashMap<String, Object> headers;
    private Formatter formatBooks;

    public AuthorTablePrinter() {
        headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("name", "Name");
        headers.put("birthYear", "Birth year");
        headers.put("books", "Books");
        formatBooks = b -> {
            List<String> books = ((Set<Book>) b).stream().map(book -> book.getTitle()).collect(Collectors.toList());
            String[] booksTitles = new String[books.size()];
            books.toArray(booksTitles);
            return booksTitles;
        };
    }

    public void printAuthorsTable(List<Author> authors) {
        TableModel model = new BeanListTableModel<>(authors, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.oldschool);
        tableBuilder.on(CellMatchers.ofType(Set.class)).addFormatter(formatBooks);
        terminal.writer().println();
        terminal.writer().println(tableBuilder.build().render(80));
    }
}
