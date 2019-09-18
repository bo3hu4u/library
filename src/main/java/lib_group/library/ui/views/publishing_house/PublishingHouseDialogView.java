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
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import lib_group.library.models.Book;
import lib_group.library.models.Location;
import lib_group.library.models.PublishingHouse;
import lib_group.library.services.BookService;
import lib_group.library.services.LocationService;
import lib_group.library.services.PublishingHouseService;
import lib_group.library.ui.editors.BookListEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class PublishingHouseDialogView extends VerticalLayout {
    @Autowired
    private PublishingHouseService publishingHouseService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private BookService bookService;
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


    public PublishingHouseDialogView(PublishingHouse publishingHouse) {
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
        setPublishingHouse(publishingHouse);
    }

    private void createHlControlButtons() {
        hlControlButtons = new HorizontalLayout();
        changePublishingHouseBtn = new Button("Edit", clickEvent -> swapToEditor());
        deletePublishingHouseBtn = new Button("Delete", clickEvent -> {
            publishingHouseService.delete(publishingHouse.getPublishHouseId());
            ((Dialog) this.getParent().get()).close();
            new Notification("deleted!").open();
        });
        saveChangePublishingHouseBtn = new Button("Save", clickEvent -> {
            publishingHouse.setName(editorName.getValue());
            publishingHouse.setBooks(vlBooksEditor.getSelectedObjects());
            if (!newAddress.isEmpty() && locationComboBox.isEmpty()) {
                publishingHouse.setLocation(new Location(newAddress));
            } else {
                publishingHouse.setLocation(locationComboBox.getValue());
            }

            ResponseEntity result = publishingHouseService.save(publishingHouse);
            if (result.getStatusCode().is4xxClientError()) {
                Label content = new Label(result.getBody().toString());
                content.getStyle().set("color", "red");
                Notification notification = new Notification(content);
                notification.setDuration(3000);
                notification.open();
            } else {
                new Notification("saved!").open();
                setValuesToComponents();
                swapToView();
            }
        });
        cancelChangePublishingHouseBtn = new Button("Cancel", clickEvent -> {
            new Notification("canceled!").open();
            swapToView();
        });
        controlButtonsVisibleFlag();
        hlControlButtons.add(changePublishingHouseBtn, deletePublishingHouseBtn, saveChangePublishingHouseBtn, cancelChangePublishingHouseBtn);
        hlControlButtons.getStyle().set("margin", "auto");
    }

    public void setPublishingHouse(PublishingHouse publishingHouse) {
        if (publishingHouse == null) {
            addAttachListener(attachEvent -> newPublishingHouseDialog());
        } else {
            this.publishingHouse = publishingHouse;
            setValuesToComponents();
        }
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
