package lib_group.library.ui.views.book;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lib_group.library.models.Author;
import lib_group.library.models.Book;
import lib_group.library.models.Description;
import lib_group.library.models.PublishingHouse;
import lib_group.library.services.interfaces.IAuthorService;
import lib_group.library.services.interfaces.IBookService;
import lib_group.library.services.interfaces.IPublishingHouseService;
import lib_group.library.ui.editors.PublishingHouseListEditor;
import lib_group.library.ui.views.IViewDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
public class BookDialogView extends VerticalLayout implements IViewDialog<Book> {
    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IPublishingHouseService publishingHouseService;
    private Book book;

    @Autowired
    private Function<List<PublishingHouse>, PublishingHouseListEditor> publishingHouseListEditorFactory;

    private HorizontalLayout hlControlButtons;
    private Button deleteBookBtn;
    private Button changeBookBtn;
    private Button saveChangeBookBtn;
    private Button cancelChangeBookBtn;
    private Button closeDialogBtn;

    private HorizontalLayout hlAllWithoutDescription;
    private VerticalLayout vlTitle;
    private Label titleLabel;
    private TextField editorTitle;

    private VerticalLayout vlEditionYear;
    private H3 editionYearHeader;
    private Label editionYearLabel;
    private TextField editorEditionYear;

    private VerticalLayout vlOnHands;
    private H3 onHandsHeader;
    private Checkbox onHands;

    private VerticalLayout vlAuthor;
    private Label authorLabel;
    private ComboBox<Author> authorComboBox;

    private VerticalLayout vlPublishingHouses;
    private H3 publishingHousesHeader;
    private VerticalLayout vlPublishingHousesList;
    private PublishingHouseListEditor vlPublishingHousesEditor;

    private Label labelDescription;
    private TextArea descriptionTextArea;

    private Dialog parentDialog;
    private Binder<Book> binder;
    private boolean editorMode;

    public void setData(Book book) {

        if (book == null) {
            addAttachListener(attachEvent -> createBookDialog());
        } else {
            this.book = book;
            setValuesToComponents();
        }
    }

    public Book getData() {
        book.setTitle(editorTitle.getValue());
        book.setEditionYear(Integer.parseInt(editorEditionYear.getValue()));
        book.setAuthor(authorComboBox.getValue());
        book.setPublishingHouses(vlPublishingHousesEditor.getSelectedObjects());
        book.setBookOnHands(onHands.getValue());
        if (!descriptionTextArea.isEmpty()) {
            if (book.getDescription() != null) {
                book.getDescription().setBookDescription(descriptionTextArea.getValue());
            } else {
                book.setDescription(new Description(book.getTitle(), descriptionTextArea.getValue()));
            }
        } else {
            book.setDescription(null);
        }
        return book;
    }

    public BookDialogView() {

        editorMode = false;
        createHlControlButtons();

        hlAllWithoutDescription = new HorizontalLayout();
        hlControlButtons.setId("hlControlButtons");
        vlTitle = new VerticalLayout();
        titleLabel = new Label();
        editorTitle = new TextField();
        vlTitle.add(new H3("Title"), titleLabel);
        vlTitle.setPadding(false);
        vlTitle.setMargin(false);

        vlEditionYear = new VerticalLayout();
        editionYearLabel = new Label();
        editorEditionYear = new TextField();
        editionYearHeader = new H3("Edition year");
        editionYearHeader.getElement().getStyle().set("width", "max-content");
        vlEditionYear.add(editionYearHeader, editionYearLabel);
        vlEditionYear.setPadding(false);
        vlEditionYear.setMargin(false);

        vlOnHands = new VerticalLayout();
        onHands = new Checkbox();
        onHandsHeader = new H3("On hands");
        onHandsHeader.getElement().getStyle().set("width", "max-content");
        vlOnHands.add(onHandsHeader, onHands);
        onHands.setEnabled(false);
        vlOnHands.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        vlOnHands.setPadding(false);
        vlOnHands.setMargin(false);

        vlAuthor = new VerticalLayout();
        authorLabel = new Label();
        authorComboBox = new ComboBox<>();
        vlAuthor.add(new H3("Author"), authorLabel);
        vlAuthor.setPadding(false);
        vlAuthor.setMargin(false);

        vlPublishingHouses = new VerticalLayout();
        publishingHousesHeader = new H3("Publishing houses");
        publishingHousesHeader.getElement().getStyle().set("width", "max-content");
        vlPublishingHousesList = new VerticalLayout();
        vlPublishingHouses.add(publishingHousesHeader, vlPublishingHousesList);
        vlPublishingHouses.setPadding(false);
        vlPublishingHouses.setMargin(false);
        vlPublishingHousesList.setPadding(false);
        vlPublishingHousesList.setMargin(false);

        hlAllWithoutDescription.setSizeFull();
        hlAllWithoutDescription.add(vlTitle, vlEditionYear, vlOnHands, vlAuthor, vlPublishingHouses);
        add(hlControlButtons, hlAllWithoutDescription);

        labelDescription = new Label();
        labelDescription.setWidth("500px");
        add(new H3("Description"), labelDescription);
        descriptionTextArea = new TextArea();
        descriptionTextArea.setSizeFull();

        binder = new Binder<>();
        binder.addStatusChangeListener(listener -> {
            Boolean noValidate = listener.hasValidationErrors();
            saveChangeBookBtn.setEnabled(!noValidate);
        });

        binder.forField(editorEditionYear).asRequired("Must be not empty").withConverter(
                new StringToIntegerConverter(" Must be Integer value..."))
                .bind(Book::getEditionYear, Book::setEditionYear);
        binder.forField(editorTitle).asRequired("Must be not empty")
                .bind(Book::getTitle, Book::setTitle);

    }

    private void createHlControlButtons() {
        hlControlButtons = new HorizontalLayout();
        changeBookBtn = new Button("Edit", clickEvent -> swapToEditor());
        deleteBookBtn = new Button("Delete", clickEvent -> {
            parentDialog = (Dialog) this.getParent().get();
            bookService.delete(book.getBookId());
            parentDialog.close();
            new Notification("deleted!").open();
        });

        saveChangeBookBtn = new Button("Save", clickEvent -> {
            book = getData();
            ResponseEntity result = bookService.save(book);
            if (result.getStatusCode().is4xxClientError()) {
                Label content = new Label(result.getBody().toString());
                content.getStyle().set("color", "red");
                Notification notification = new Notification(content);
                notification.setDuration(3000);
                notification.open();
            } else {
                new Notification("saved!").open();
                book = (Book) bookService.getById(book.getBookId()).getBody();
                setValuesToComponents();
                swapToView();
            }
        });
        cancelChangeBookBtn = new Button("Cancel", clickEvent -> {
            new Notification("canceled!").open();
            swapToView();
        });
        closeDialogBtn = new Button("Close", clickEvent -> ((Dialog) this.getParent().get()).close());
        controlButtonsVisibleFlag();
        hlControlButtons.add(changeBookBtn, deleteBookBtn, saveChangeBookBtn, cancelChangeBookBtn, closeDialogBtn);
        hlControlButtons.getStyle().set("margin", "auto");
    }

    private void setValuesToComponents() {
        titleLabel.setText(book.getTitle());
        editionYearLabel.setText(book.getEditionYear().toString());
        onHands.setValue(book.isBookOnHands());
        authorLabel.setText(book.getAuthor() != null ? book.getAuthor().getName() : "Not specified");
        vlPublishingHousesList.removeAll();
        if (!book.getPublishingHouses().isEmpty()) {
            for (PublishingHouse publishingHouse : book.getPublishingHouses()) {
                Label publishingHouseName = new Label(publishingHouse.getName());
                vlPublishingHousesList.add(publishingHouseName);
            }
        } else {
            vlPublishingHousesList.add(new Label("No publishing houses"));
        }
        labelDescription.setText(book.getDescriptionId() != null ? book.getDescription().getBookDescription() : "No description");
    }

    private void controlButtonsVisibleFlag() {
        changeBookBtn.setVisible(!editorMode);
        deleteBookBtn.setVisible(!editorMode);
        saveChangeBookBtn.setVisible(editorMode);
        cancelChangeBookBtn.setVisible(editorMode);
    }

    private void swapToView() {
        editorMode = false;
        controlButtonsVisibleFlag();
        vlTitle.replace(editorTitle, titleLabel);
        vlEditionYear.replace(editorEditionYear, editionYearLabel);
        onHands.setEnabled(false);
        vlAuthor.replace(authorComboBox, authorLabel);
        vlPublishingHouses.replace(vlPublishingHousesEditor, vlPublishingHousesList);
        replace(descriptionTextArea, labelDescription);

    }

    private void swapToEditor() {
        editorMode = true;
        controlButtonsVisibleFlag();

        editorTitle.setValue(book.getTitle());
        vlTitle.replace(titleLabel, editorTitle);

        editorEditionYear.setValue(book.getEditionYear().toString());
        vlEditionYear.replace(editionYearLabel, editorEditionYear);

        onHands.setEnabled(true);
        authorComboBox.setItemLabelGenerator(author -> author.getName());
        authorComboBox.setItems(authorService.getAll());
        authorComboBox.setValue(book.getAuthor());
        vlAuthor.replace(authorLabel, authorComboBox);

        List<PublishingHouse> bookPublishingHouses = new ArrayList<>(book.getPublishingHouses());
        List<PublishingHouse> allPh = publishingHouseService.getAll();
        vlPublishingHousesEditor = publishingHouseListEditorFactory.apply(allPh);
        vlPublishingHousesEditor.setComboBoxesForObjects(bookPublishingHouses);
        vlPublishingHouses.replace(vlPublishingHousesList, vlPublishingHousesEditor);
        if (book.getDescription() != null) {
            descriptionTextArea.setValue(book.getDescription().getBookDescription());
        }
        replace(labelDescription, descriptionTextArea);
    }

    private void createBookDialog() {
        book = new Book();
        editorMode = true;
        controlButtonsVisibleFlag();
        cancelChangeBookBtn.setVisible(false);

        vlTitle.replace(titleLabel, editorTitle);
        onHands.setEnabled(true);
        vlEditionYear.replace(editionYearLabel, editorEditionYear);

        authorComboBox.setItemLabelGenerator(author -> author.getName());
        authorComboBox.setItems(authorService.getAll());
        vlAuthor.replace(authorLabel, authorComboBox);

        vlPublishingHousesEditor = publishingHouseListEditorFactory.apply(publishingHouseService.getAll());
        vlPublishingHouses.replace(vlPublishingHousesList, vlPublishingHousesEditor);

        replace(labelDescription, descriptionTextArea);
    }

}
