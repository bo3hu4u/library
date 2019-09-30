package terminal.utils.printer;

import models.Author;
import models.Book;
import models.Location;
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
public class PublishingHouseTablePrinter {
    @Autowired
    @Lazy
    private Terminal terminal;
    private LinkedHashMap<String, Object> headers;
    private Formatter formatBooks;
    private Formatter formatLocation;

    public PublishingHouseTablePrinter() {
        headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("name", "Name");
        headers.put("location", "Location");
        headers.put("books", "Books");
        formatLocation = a -> {
            String[] str = new String[1];
            str[0] = ((Location) a).getAddress();
            return str;
        };
        formatBooks = b -> {
            List<String> books = ((Set<Book>) b).stream().map(book -> book.getTitle()).collect(Collectors.toList());
            String[] booksTitles = new String[books.size()];
            books.toArray(booksTitles);
            return booksTitles;
        };
    }

    public void printPublishingHousesTable(List<PublishingHouse> publishingHouses) {
        TableModel model = new BeanListTableModel<>(publishingHouses, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.oldschool);
        tableBuilder.on(CellMatchers.ofType(Set.class)).addFormatter(formatBooks);
        tableBuilder.on(CellMatchers.ofType(Location.class)).addFormatter(formatLocation);
        terminal.writer().println();
        terminal.writer().println(tableBuilder.build().render(80));
    }
}
