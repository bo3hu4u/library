package terminal.utils.printer;

import models.Author;
import models.Book;
import models.Description;
import models.PublishingHouse;
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
public class BookTablePrinter {
    @Autowired
    @Lazy
    private Terminal terminal;
    private LinkedHashMap<String, Object> headers;
    private Formatter formatPublishingHouses;
    private Formatter formatAuthor;
    private Formatter formatDescription;

    public BookTablePrinter() {
        headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("title", "Title");
        headers.put("author", "Author");
        headers.put("description", "Description");
        headers.put("publishingHouses", "Publishing houses");
        headers.put("editionYear", "Edition year");
        headers.put("bookOnHands", "On hands?");

        formatAuthor = a -> {
            String[] str = new String[1];
            str[0] = ((Author) a).getName();
            return str;
        };
        formatDescription = d -> {
            String[] str = new String[1];
            str[0] = ((Description) d).getBookDescription();
            return str;
        };
        formatPublishingHouses = p -> {
            List<String> publishingHouses = ((Set<PublishingHouse>) p).stream().map(pH -> pH.getName()).collect(Collectors.toList());
            String[] publishingHousesNames = new String[publishingHouses.size()];
            publishingHouses.toArray(publishingHousesNames);
            return publishingHousesNames;
        };
    }

    public void printBooksTable(List<Book> books) {
        TableModel model = new BeanListTableModel<>(books, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.oldschool);

        tableBuilder.on(CellMatchers.ofType(Author.class)).addFormatter(formatAuthor);
        tableBuilder.on(CellMatchers.ofType(Description.class)).addFormatter(formatDescription);
        tableBuilder.on(CellMatchers.ofType(Set.class)).addFormatter(formatPublishingHouses);
        terminal.writer().println();
        terminal.writer().println(tableBuilder.build().render(140));
    }
}
