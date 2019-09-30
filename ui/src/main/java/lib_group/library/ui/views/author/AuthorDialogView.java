package lib_group.library.ui.views.author;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import models.Author;
import models.Book;
import services.interfaces.IAuthorService;
import services.interfaces.IBookService;
import lib_group.library.ui.editors.BookListEditor;
import lib_group.library.ui.views.IViewDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
public class AuthorDialogView extends VerticalLayout implements IViewDialog<Author> {

    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IBookService bookService;
    private Author author;

    private HorizontalLayout hlControlButtons;
    private Button deleteAuthorBtn;
    private Button changeAuthorBtn;
    private Button saveChangeAuthorBtn;
    private Button cancelChangeAuthorBtn;
    private Button closeDialogBtn;

    private HorizontalLayout authorInfo;
    private VerticalLayout vlName;
    private H3 nameHeader;
    private Label nameLabel;
    private TextField editorName;

    private VerticalLayout vlBirthYear;
    private H3 birthYearHeader;
    private Label birthYearLabel;
    private TextField editorBirthYear;

    private VerticalLayout vlBooks;
    private H3 booksHeader;
    private VerticalLayout vlBooksList;
    private BookListEditor vlBooksEditor;

    @Autowired
    private Function<List<Book>, BookListEditor> bookListEditorFactory;

    private boolean editorMode;
    private Binder<Author> binder;

    public AuthorDialogView() {
        editorMode = false;

        createHlControlButtons();
        authorInfo = new HorizontalLayout();

        vlName = new VerticalLayout();
        nameLabel = new Label();
        nameHeader = new H3("Name");
        editorName = new TextField();
        vlName.add(nameHeader, nameLabel);
        vlName.setPadding(false);
        vlName.setMargin(false);

        vlBirthYear = new VerticalLayout();
        birthYearLabel = new Label();
        birthYearHeader = new H3("Birth year");
        birthYearHeader.getElement().getStyle().set("width", "max-content");
        editorBirthYear = new TextField();
        vlBirthYear.add(birthYearHeader, birthYearLabel);
        vlBirthYear.setPadding(false);
        vlBirthYear.setMargin(false);

        vlBooks = new VerticalLayout();
        booksHeader = new H3("Books");
        vlBooksList = new VerticalLayout();
        vlBooks.add(booksHeader, vlBooksList);
        vlBooks.setPadding(false);
        vlBooks.setMargin(false);
        vlBooksList.setPadding(false);
        vlBooksList.setMargin(false);
        authorInfo.add(vlName, vlBirthYear, vlBooks);
        add(hlControlButtons, authorInfo);

        binder = new Binder<>();
        binder.addStatusChangeListener(listener -> {
            Boolean noValidate = listener.hasValidationErrors();
            saveChangeAuthorBtn.setEnabled(!noValidate);
        });

        binder.forField(editorBirthYear).asRequired("Must be not empty").withConverter(
                new StringToIntegerConverter(" Must be Integer value..."))
                .bind(Author::getBirthYear, Author::setBirthYear);
        binder.forField(editorName).asRequired("Must be not empty")
                .bind(Author::getName, Author::setName);


    }

    private void createHlControlButtons() {
        hlControlButtons = new HorizontalLayout();
        changeAuthorBtn = new Button("Edit", clickEvent -> swapToEditor());
        deleteAuthorBtn = new Button("Delete", clickEvent -> {
            authorService.delete(author.getId());
            ((Dialog) this.getParent().get()).close();
            new Notification("deleted!").open();
        });

        saveChangeAuthorBtn = new Button("Save", clickEvent -> {
            author = getData();
            try {
                authorService.save(author);
                new Notification("saved!").open();
                // author = (Author) authorService.getById(author.getId()).getBody();
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

        cancelChangeAuthorBtn = new Button("Cancel", clickEvent -> {
            swapToView();
            new Notification("canceled!").open();
        });
        closeDialogBtn = new Button("Close", clickEvent -> ((Dialog) this.getParent().get()).close());
        controlButtonsVisibleFlag();
        hlControlButtons.add(changeAuthorBtn, deleteAuthorBtn, saveChangeAuthorBtn, cancelChangeAuthorBtn, closeDialogBtn);
        hlControlButtons.getStyle().set("margin", "auto");
    }

    public Author getData() {
        author.setName(editorName.getValue());
        author.setBirthYear(Integer.parseInt(editorBirthYear.getValue()));
        author.setBooks(vlBooksEditor.getSelectedObjects());
        return author;
    }

    public void setData(Author author) {
        if (author == null) {
            addAttachListener(attachEvent -> newAuthorDialog());
        } else {
            this.author = author;
            setValuesToComponents();
        }
    }

    private void controlButtonsVisibleFlag() {
        changeAuthorBtn.setVisible(!editorMode);
        deleteAuthorBtn.setVisible(!editorMode);
        saveChangeAuthorBtn.setVisible(editorMode);
        cancelChangeAuthorBtn.setVisible(editorMode);
    }

    private void setValuesToComponents() {
        nameLabel.setText(author.getName());
        birthYearLabel.setText(author.getBirthYear().toString());

        vlBooksList.removeAll();
        if (!author.getBooks().isEmpty()) {
            for (Book book : author.getBooks()) {
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


    public void swapToView() {
        editorMode = false;
        controlButtonsVisibleFlag();
        vlName.replace(editorName, nameLabel);
        vlBirthYear.replace(editorBirthYear, birthYearLabel);
        vlBooks.replace(vlBooksEditor, vlBooksList);

    }

    public void swapToEditor() {
        editorMode = true;
        controlButtonsVisibleFlag();

        editorName.setValue(author.getName());
        vlName.replace(nameLabel, editorName);

        editorBirthYear.setValue(author.getBirthYear().toString());
        vlBirthYear.replace(birthYearLabel, editorBirthYear);

        List<Book> authorBooks = new ArrayList<>(author.getBooks());
        vlBooksEditor = bookListEditorFactory.apply(bookService.getAll());
        vlBooksEditor.setComboBoxesForObjects(authorBooks);
        vlBooks.replace(vlBooksList, vlBooksEditor);
    }

    public void newAuthorDialog() {
        author = new Author();
        editorMode = true;
        controlButtonsVisibleFlag();
        cancelChangeAuthorBtn.setVisible(false);

        vlName.replace(nameLabel, editorName);
        vlBirthYear.replace(birthYearLabel, editorBirthYear);
        vlBooksEditor = bookListEditorFactory.apply(bookService.getAll());
        vlBooks.replace(vlBooksList, vlBooksEditor);

    }
}
