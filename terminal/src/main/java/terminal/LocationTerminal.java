package terminal;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import exceptions.NotFoundEntityException;
import models.Location;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import services.interfaces.ILocationService;
import terminal.utils.printer.LocationTablePrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class LocationTerminal {
    @Autowired
    @Lazy
    private Terminal terminal;
    @Autowired
    @Lazy
    private LineReader lineReader;
    @Autowired
    private ILocationService locationService;
    @Autowired
    private LocationTablePrinter locationTablePrinter;

    @ShellMethod("Get all locations")
    public void getAllLocations() {
        List<Location> locations = locationService.getAll();
        locationTablePrinter.printLocationTable(locations);
    }

    @ShellMethod("get location by id")
    public void getLocationById(Long Id) {
        try {
            List<Location> locationList = new ArrayList<>();
            locationList.add(locationService.getById(Id));
            locationTablePrinter.printLocationTable(locationList);
        } catch (NotFoundEntityException exc) {
            terminal.writer().println(exc.getMessage());
        }
    }

    @ShellMethod("Add new location")
    public void addLocation(Location location) {
        try {
            if (location != null) {
                locationService.save(location);
                List<Location> locationList = new ArrayList<>();
                locationList.add(location);
                locationTablePrinter.printLocationTable(locationList);
            }
        } catch (DataIntegrityViolationException e) {
            terminal.writer().println(e.getRootCause().getMessage());
        } catch (NotFoundEntityException exc) {
            terminal.writer().println(exc.getMessage());
        }
    }

    @ShellMethod("Update location")
    public void updateLocation(Long Id, String json) throws IOException {
        try {
            Location location = locationService.updateLocation(Id, json);
            List<Location> locationList = new ArrayList<>();
            locationList.add(location);
            locationTablePrinter.printLocationTable(locationList);
        } catch (DataIntegrityViolationException e) {
            terminal.writer().println(e.getRootCause().getMessage());
        } catch (InvalidFormatException | NotFoundEntityException ifException) {
            terminal.writer().println(ifException.getMessage());
        }

    }


    @ShellMethod("delete location")
    public void deleteLocation(Long Id) {
        try {
            while (true) {
                terminal.writer().println("delete location with id: " + Id + "? Y/N ");
                String answer = lineReader.readLine();
                if (answer.equalsIgnoreCase("Y")) {
                    locationService.delete(Id);
                    terminal.writer().println("location deleted");
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


}
