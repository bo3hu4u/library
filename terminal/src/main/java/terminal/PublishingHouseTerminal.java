package terminal;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.NotFoundEntityException;
import models.PublishingHouse;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import services.interfaces.IBookService;
import services.interfaces.IPublishingHouseService;
import terminal.utils.printer.BookTablePrinter;
import terminal.utils.printer.PublishingHouseTablePrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class PublishingHouseTerminal {
    @Autowired
    @Lazy
    private Terminal terminal;
    @Autowired
    @Lazy
    private LineReader lineReader;
    @Autowired
    private IPublishingHouseService publishingHouseService;
    @Autowired
    private PublishingHouseTablePrinter publishingHouseTablePrinter;

    @ShellMethod("Get all publishing houses")
    public void getAllPublishingHouses() {
        List<PublishingHouse> publishingHouses = publishingHouseService.getAll();
        publishingHouseTablePrinter.printPublishingHousesTable(publishingHouses);
    }

    @ShellMethod("Get publishing house buy id")
    public void getPublishingHouseById(Long Id) {
        try {
            List<PublishingHouse> houseList = new ArrayList<>();
            houseList.add(publishingHouseService.getById(Id));
            publishingHouseTablePrinter.printPublishingHousesTable(houseList);
        } catch (NotFoundEntityException exc) {
            terminal.writer().println(exc.getMessage());
        }
    }


    @ShellMethod("Add new publishing house")
    public void addPublishingHouse(PublishingHouse publishingHouse) {
        try {
            if (publishingHouse != null) {
                publishingHouseService.save(publishingHouse);
                List<PublishingHouse> houseList = new ArrayList<>();
                houseList.add(publishingHouse);
                publishingHouseTablePrinter.printPublishingHousesTable(houseList);
            }
        } catch (DataIntegrityViolationException e) {
            terminal.writer().println(e.getRootCause().getMessage());
        } catch (NotFoundEntityException exc) {
            terminal.writer().println(exc.getMessage());
        }
    }


    @ShellMethod("Delete publishing house")
    public void deletePublishingHouse(Long Id) {
        try {
            while (true) {
                terminal.writer().println("delete publishing house with id: " + Id + "? Y/N ");
                String answer = lineReader.readLine();
                if (answer.equalsIgnoreCase("Y")) {
                    publishingHouseService.delete(Id);
                    terminal.writer().println("publishing house deleted");
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

    @ShellMethod("Update publishing house")
    public void updatePublishingHouse(Long Id, String json, String flag) throws IOException {
        try {
            PublishingHouse publishingHouse = publishingHouseService.updateFromJson(Id, json, flag);
            List<PublishingHouse> houseList = new ArrayList<>();
            houseList.add(publishingHouse);
            publishingHouseTablePrinter.printPublishingHousesTable(houseList);
        } catch (DataIntegrityViolationException e) {
            terminal.writer().println(e.getRootCause().getMessage());
        } catch (InvalidFormatException | NotFoundEntityException ifException) {
            terminal.writer().println(ifException.getMessage());
        }
    }


}

