<<<<<<< HEAD
package lib_group.library.ui.views.publishing_house;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import models.Book;
import models.Location;
import models.PublishingHouse;
import services.interfaces.IBookService;
import services.interfaces.ILocationService;
import services.interfaces.IPublishingHouseService;
import lib_group.library.ui.editors.BookListEditor;
import lib_group.library.ui.views.IViewDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

public class PublishingHouseDialogView extends VerticalLayout implements IViewDialog<PublishingHouse> {
    @Autowired
    private IPublishingHouseService publishingHouseService;
    @Autowired
    private ILocationService locationService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private Function<List<Book>, BookListEditor> bookListEditorFactory;

    private PublishingHouse publishingHouse;
    private String newAddress;
    private boolean editorMode;

    private HorizontalLayout hlControlButtons;
    private Button deletePublishingHouseBtn;
    private Button changePublishingHouseBtn;
    private Button saveChangePublishingHouseBtn;
    private Button cancelChangePublishingHouseBtn;
    private Button closeDialogBtn;

    private HorizontalLayout publishingHouseInfo;
    private VerticalLayout vlName;
    private H3 nameHeader;
    private Label nameLabel;
    private TextField editorName;

    private VerticalLayout vlLocation;
    private H3 locationHeader;
    private Label locationLabel;
    private ComboBox<Location> locationComboBox;

    private VerticalLayout vlBooks;
    private H3 booksHeader;
    private VerticalLayout vlBooksList;
    private BookListEditor vlBooksEditor;

    private Binder<PublishingHouse> binder;


    public PublishingHouseDialogView() {
        newAddress = "";
        editorMode = false;

        createHlControlButtons();
        publishingHouseInfo = new HorizontalLayout();
        vlName = new VerticalLayout();
        nameHeader = new H3("Name");
        editorName = new TextField();
        nameLabel = new Label();
        vlName.add(nameHeader, nameLabel);
        vlName.setPadding(false);
        vlName.setMargin(false);

        vlLocation = new VerticalLayout();
        locationHeader = new H3("Location");
        locationHeader.getElement().getStyle().set("width", "max-content");
        locationLabel = new Label();
        locationComboBox = new ComboBox<>();
        locationComboBox.addCustomValueSetListener(comboBoxCustomValueSetEvent -> newAddress = comboBoxCustomValueSetEvent.getDetail());
        vlLocation.add(locationHeader, locationLabel);
        vlLocation.setPadding(false);
        vlLocation.setMargin(false);

        vlBooks = new VerticalLayout();
        booksHeader = new H3("Books");
        vlBooksList = new VerticalLayout();
        vlBooks.add(booksHeader, vlBooksList);
        vlBooks.setPadding(false);
        vlBooks.setMargin(false);
        vlBooksList.setPadding(false);
        vlBooksList.setMargin(false);
        publishingHouseInfo.add(vlName, vlLocation, vlBooks);
        add(hlControlButtons, publishingHouseInfo);

        binder = new Binder<>();
        binder.addStatusChangeListener(listener -> {
            Boolean noValidate = listener.hasValidationErrors();
            saveChangePublishingHouseBtn.setEnabled(!noValidate);
        });

        binder.forField(editorName).asRequired("Must be not empty")
                .bind(PublishingHouse::getName, PublishingHouse::setName);
    }

    private void createHlControlButtons() {
        hlControlButtons = new HorizontalLayout();
        changePublishingHouseBtn = new Button("Edit", clickEvent -> swapToEditor());
        deletePublishingHouseBtn = new Button("Delete", clickEvent -> {
            publishingHouseService.delete(publishingHouse.getId());
            ((Dialog) this.getParent().get()).close();
            new Notification("deleted!").open();
        });
        saveChangePublishingHouseBtn = new Button("Save", clickEvent -> {
            publishingHouse = getData();
            try {
                publishingHouse = publishingHouseService.save(publishingHouse);
                new Notification("saved!").open();
                setValuesToComponents();
                swapToView();
            } catch (DataIntegrityViolationException exc) {
                Label content = new Label(exc.getMostSpecificCause().getMessage());
                content.getStyle().set("color", "red");
                Notification notification = new Notification(content);
                notification.setDuration(3000);
                notification.open();
            }
        });
        cancelChangePublishingHouseBtn = new Button("Cancel", clickEvent -> {
            new Notification("canceled!").open();
            swapToView();
        });
        closeDialogBtn = new Button("Close", clickEvent -> ((Dialog) this.getParent().get()).close());
        controlButtonsVisibleFlag();
        hlControlButtons.add(changePublishingHouseBtn, deletePublishingHouseBtn, saveChangePublishingHouseBtn, cancelChangePublishingHouseBtn, closeDialogBtn);
        hlControlButtons.getStyle().set("margin", "auto");
    }

    public void setData(PublishingHouse publishingHouse) {
        if (publishingHouse == null) {
            addAttachListener(attachEvent -> newPublishingHouseDialog());
        } else {
            this.publishingHouse = publishingHouse;
            setValuesToComponents();
        }
    }

    @Override
    public PublishingHouse getData() {
        publishingHouse.setName(editorName.getValue());
        publishingHouse.setBooks(vlBooksEditor.getSelectedObjects());
        if (!newAddress.isEmpty() && locationComboBox.isEmpty()) {
            publishingHouse.setLocation(new Location(newAddress));
        } else {
            publishingHouse.setLocation(locationComboBox.getValue());
        }
        return publishingHouse;
    }

    private void setValuesToComponents() {
        nameLabel.setText(publishingHouse.getName());
        locationLabel.setText(publishingHouse.getLocation() != null ? publishingHouse.getLocation().getAddress() : "No address");

        vlBooksList.removeAll();
        if (!publishingHouse.getBooks().isEmpty()) {
            for (Book book : publishingHouse.getBooks()) {
                Label bookTitle = new Label(book.getTitle());
                bookTitle.getElement().getStyle().set("width", "max-content");
                vlBooksList.add(bookTitle);
            }
        } else {
            Label noBooks = new Label("No books");
            noBooks.getElement().getStyle().set("width", "max-content");
            vlBooksList.add(noBooks);
        }
    }

    private void controlButtonsVisibleFlag() {
        changePublishingHouseBtn.setVisible(!editorMode);
        deletePublishingHouseBtn.setVisible(!editorMode);
        saveChangePublishingHouseBtn.setVisible(editorMode);
        cancelChangePublishingHouseBtn.setVisible(editorMode);
    }

    public void swapToView() {
        editorMode = false;
        controlButtonsVisibleFlag();
        vlName.replace(editorName, nameLabel);
        vlLocation.replace(locationComboBox, locationLabel);
        vlBooks.replace(vlBooksEditor, vlBooksList);
    }

    public void swapToEditor() {
        editorMode = true;
        controlButtonsVisibleFlag();

        editorName.setValue(publishingHouse.getName());
        vlName.replace(nameLabel, editorName);

        locationComboBox.setItemLabelGenerator(location -> location.getAddress());
        locationComboBox.setItems(locationService.getAll());
        locationComboBox.setValue(publishingHouse.getLocation());
        vlLocation.replace(locationLabel, locationComboBox);

        List<Book> publishingHouseBooks = new ArrayList<>(publishingHouse.getBooks());
        vlBooksEditor = bookListEditorFactory.apply(bookService.getAll());
        vlBooksEditor.setComboBoxesForObjects(publishingHouseBooks);
        vlBooks.replace(vlBooksList, vlBooksEditor);
    }

    public void newPublishingHouseDialog() {
        publishingHouse = new PublishingHouse();
        editorMode = true;
        controlButtonsVisibleFlag();
        cancelChangePublishingHouseBtn.setVisible(false);

        vlName.replace(nameLabel, editorName);

        locationComboBox.setItemLabelGenerator(location -> location.getAddress());
        locationComboBox.setItems(locationService.getAll());
        vlLocation.replace(locationLabel, locationComboBox);

        vlBooksEditor = bookListEditorFactory.apply(bookService.getAll());
        vlBooks.replace(vlBooksList, vlBooksEditor);
    }

}
=======
package lib_group.library.ui.views.publishing_house;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import models.Book;
import models.Location;
import models.PublishingHouse;
import services.interfaces.IBookService;
import services.interfaces.ILocationService;
import services.interfaces.IPublishingHouseService;
import lib_group.library.ui.editors.BookListEditor;
import lib_group.library.ui.views.IViewDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

public class PublishingHouseDialogView extends VerticalLayout implements IViewDialog<PublishingHouse> {
    @Autowired
    private IPublishingHouseService publishingHouseService;
    @Autowired
    private ILocationService locationService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private Function<List<Book>, BookListEditor> bookListEditorFactory;

    private PublishingHouse publishingHouse;
    private String newAddress;
    private boolean editorMode;

    private HorizontalLayout hlControlButtons;
    private Button deletePublishingHouseBtn;
    private Button changePublishingHouseBtn;
    private Button saveChangePublishingHouseBtn;
    private Button cancelChangePublishingHouseBtn;
    private Button closeDialogBtn;

    private HorizontalLayout publishingHouseInfo;
    private VerticalLayout vlName;
    private H3 nameHeader;
    private Label nameLabel;
    private TextField editorName;

    private VerticalLayout vlLocation;
    private H3 locationHeader;
    private Label locationLabel;
    private ComboBox<Location> locationComboBox;

    private VerticalLayout vlBooks;
    private H3 booksHeader;
    private VerticalLayout vlBooksList;
    private BookListEditor vlBooksEditor;

    private Binder<PublishingHouse> binder;


    public PublishingHouseDialogView() {
        newAddress = "";
        editorMode = false;

        createHlControlButtons();
        publishingHouseInfo = new HorizontalLayout();
        vlName = new VerticalLayout();
        nameHeader = new H3("Name");
        editorName = new TextField();
        nameLabel = new Label();
        vlName.add(nameHeader, nameLabel);
        vlName.setPadding(false);
        vlName.setMargin(false);

        vlLocation = new VerticalLayout();
        locationHeader = new H3("Location");
        locationHeader.getElement().getStyle().set("width", "max-content");
        locationLabel = new Label();
        locationComboBox = new ComboBox<>();
        locationComboBox.addCustomValueSetListener(comboBoxCustomValueSetEvent -> newAddress = comboBoxCustomValueSetEvent.getDetail());
        vlLocation.add(locationHeader, locationLabel);
        vlLocation.setPadding(false);
        vlLocation.setMargin(false);

        vlBooks = new VerticalLayout();
        booksHeader = new H3("Books");
        vlBooksList = new VerticalLayout();
        vlBooks.add(booksHeader, vlBooksList);
        vlBooks.setPadding(false);
        vlBooks.setMargin(false);
        vlBooksList.setPadding(false);
        vlBooksList.setMargin(false);
        publishingHouseInfo.add(vlName, vlLocation, vlBooks);
        add(hlControlButtons, publishingHouseInfo);

        binder = new Binder<>();
        binder.addStatusChangeListener(listener -> {
            Boolean noValidate = listener.hasValidationErrors();
            saveChangePublishingHouseBtn.setEnabled(!noValidate);
        });

        binder.forField(editorName).asRequired("Must be not empty")
                .bind(PublishingHouse::getName, PublishingHouse::setName);
    }

    private void createHlControlButtons() {
        hlControlButtons = new HorizontalLayout();
        changePublishingHouseBtn = new Button("Edit", clickEvent -> swapToEditor());
        deletePublishingHouseBtn = new Button("Delete", clickEvent -> {
            publishingHouseService.delete(publishingHouse.getId());
            ((Dialog) this.getParent().get()).close();
            new Notification("deleted!").open();
        });
        saveChangePublishingHouseBtn = new Button("Save", clickEvent -> {
            publishingHouse = getData();
            try {
                publishingHouse = publishingHouseService.save(publishingHouse);
                new Notification("saved!").open();
                setValuesToComponents();
                swapToView();
            } catch (DataIntegrityViolationException exc) {
                Label content = new Label(exc.getMostSpecificCause().getMessage());
                content.getStyle().set("color", "red");
                Notification notification = new Notification(content);
                notification.setDuration(3000);
                notification.open();
            }
        });
        cancelChangePublishingHouseBtn = new Button("Cancel", clickEvent -> {
            new Notification("canceled!").open();
            swapToView();
        });
        closeDialogBtn = new Button("Close", clickEvent -> ((Dialog) this.getParent().get()).close());
        controlButtonsVisibleFlag();
        hlControlButtons.add(changePublishingHouseBtn, deletePublishingHouseBtn, saveChangePublishingHouseBtn, cancelChangePublishingHouseBtn, closeDialogBtn);
        hlControlButtons.getStyle().set("margin", "auto");
    }

    public void setData(PublishingHouse publishingHouse) {
        if (publishingHouse == null) {
            addAttachListener(attachEvent -> newPublishingHouseDialog());
        } else {
            this.publishingHouse = publishingHouse;
            setValuesToComponents();
        }
    }

    @Override
    public PublishingHouse getData() {
        publishingHouse.setName(editorName.getValue());
        publishingHouse.setBooks(vlBooksEditor.getSelectedObjects());
        if (!newAddress.isEmpty() && locationComboBox.isEmpty()) {
            publishingHouse.setLocation(new Location(newAddress));
        } else {
            publishingHouse.setLocation(locationComboBox.getValue());
        }
        return publishingHouse;
    }

    private void setValuesToComponents() {
        nameLabel.setText(publishingHouse.getName());
        locationLabel.setText(publishingHouse.getLocation() != null ? publishingHouse.getLocation().getAddress() : "No address");

        vlBooksList.removeAll();
        if (!publishingHouse.getBooks().isEmpty()) {
            for (Book book : publishingHouse.getBooks()) {
                Label bookTitle = new Label(book.getTitle());
                bookTitle.getElement().getStyle().set("width", "max-content");
                vlBooksList.add(bookTitle);
            }
        } else {
            Label noBooks = new Label("No books");
            noBooks.getElement().getStyle().set("width", "max-content");
            vlBooksList.add(noBooks);
        }
    }

    private void controlButtonsVisibleFlag() {
        changePublishingHouseBtn.setVisible(!editorMode);
        deletePublishingHouseBtn.setVisible(!editorMode);
        saveChangePublishingHouseBtn.setVisible(editorMode);
        cancelChangePublishingHouseBtn.setVisible(editorMode);
    }

    public void swapToView() {
        editorMode = false;
        controlButtonsVisibleFlag();
        vlName.replace(editorName, nameLabel);
        vlLocation.replace(locationComboBox, locationLabel);
        vlBooks.replace(vlBooksEditor, vlBooksList);
    }

    public void swapToEditor() {
        editorMode = true;
        controlButtonsVisibleFlag();

        editorName.setValue(publishingHouse.getName());
        vlName.replace(nameLabel, editorName);

        locationComboBox.setItemLabelGenerator(location -> location.getAddress());
        locationComboBox.setItems(locationService.getAll());
        locationComboBox.setValue(publishingHouse.getLocation());
        vlLocation.replace(locationLabel, locationComboBox);

        List<Book> publishingHouseBooks = new ArrayList<>(publishingHouse.getBooks());
        vlBooksEditor = bookListEditorFactory.apply(bookService.getAll());
        vlBooksEditor.setComboBoxesForObjects(publishingHouseBooks);
        vlBooks.replace(vlBooksList, vlBooksEditor);
    }

    public void newPublishingHouseDialog() {
        publishingHouse = new PublishingHouse();
        editorMode = true;
        controlButtonsVisibleFlag();
        cancelChangePublishingHouseBtn.setVisible(false);

        vlName.replace(nameLabel, editorName);

        locationComboBox.setItemLabelGenerator(location -> location.getAddress());
        locationComboBox.setItems(locationService.getAll());
        vlLocation.replace(locationLabel, locationComboBox);

        vlBooksEditor = bookListEditorFactory.apply(bookService.getAll());
        vlBooks.replace(vlBooksList, vlBooksEditor);
    }

}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
