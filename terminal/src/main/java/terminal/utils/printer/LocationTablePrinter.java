package terminal.utils.printer;

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

@Component
public class LocationTablePrinter {

    @Autowired
    @Lazy
    private Terminal terminal;
    private LinkedHashMap<String, Object> headers;
    private Formatter formatPublishingHouse;

    public LocationTablePrinter() {
        headers = new LinkedHashMap<>();
        headers.put("id", "Id");
        headers.put("address", "Address");
        headers.put("publishingHouse", "Publishing house");
        formatPublishingHouse = a -> {
            String[] str = new String[1];
            str[0] = ((PublishingHouse) a).getName();
            return str;
        };
    }

    public void printLocationTable(List<Location> locations) {
        TableModel model = new BeanListTableModel<>(locations, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.oldschool);
        tableBuilder.on(CellMatchers.ofType(PublishingHouse.class)).addFormatter(formatPublishingHouse);
        terminal.writer().println();
        terminal.writer().println(tableBuilder.build().render(80));
    }
}
