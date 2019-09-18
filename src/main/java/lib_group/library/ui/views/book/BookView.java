package lib_group.library.ui.views.book;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lib_group.library.models.Book;
import lib_group.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;

@Route("ui/books")
public class BookView extends VerticalLayout {
    @Autowired
    private Function<Book, BookDialogView> bookDialogViewFactory;

    private Dialog dialog;
    private Grid<Book> grid;
    private Book book;
    private BookDialogView bookDialogView;
    private Button newBookBtn;

    public BookView(@Autowired BookService bookService) {

        dialog = new Dialog();
        grid = new Grid<>();

        grid.addColumn(book -> book.getAuthor() != null ? book.getAuthor().getName() : "Not specified").setHeader("Author").setKey("author");
        grid.addColumn(book -> book.getTitle()).setHeader("Title").setKey("title");
        grid.addComponentColumn(book -> {
            Label description = new Label();
            description.getElement().getStyle().set("white-space", "pre-wrap").set("word-wrap", "break-word");
            if (book.getDescObj() != null) {
                description.setText(book.getDescObj().getBookDescription());
            } else {
                description.setText("No description");
            }
            return description;
        }).setHeader("Description").setKey("description").setFlexGrow(3);
        grid.addColumn(book -> book.getEditionYear()).setHeader("Edition year").setKey("editionYear");
        grid.addComponentColumn(book -> {
            VerticalLayout bookPublishingHouses = new VerticalLayout();
            if (book.getPublishingHouses().size() > 2) {
                bookPublishingHouses.setHeight("100px");
            }
            bookPublishingHouses.getElement().getStyle().set("overflow", "auto");
            book.getPublishingHouses().forEach(publishingHouse -> bookPublishingHouses.add(new Label(publishingHouse.getName())));
            return bookPublishingHouses;
        }).setHeader("Publishing houses").setKey("publishingHouse");
        grid.addComponentColumn(book -> {
            HorizontalLayout hlOnHandsBox = new HorizontalLayout();
            hlOnHandsBox.getStyle().set("display", "block");
            Checkbox onHands = new Checkbox();
            onHands.setValue(book.isBookOnHands());
            hlOnHandsBox.add(onHands);

            onHands.addValueChangeListener(listener -> {
                if (hlOnHandsBox.getChildren().count() < 2) {
                    Button updateOnHandsBtn = new Button();
                    Boolean old = listener.getOldValue();
                    updateOnHandsBtn.addClickListener(buttonClickEvent -> {
                        if (onHands.getValue() ^ old) {
                            book.setBookOnHands(onHands.getValue());
                            bookService.save(book);
                        } else {
                            new Notification("Value not changed").open();
                        }
                        hlOnHandsBox.remove(updateOnHandsBtn);
                    });
                    updateOnHandsBtn.setIcon(new Icon(VaadinIcon.REFRESH));
                    hlOnHandsBox.add(updateOnHandsBtn);
                }
            });
            return hlOnHandsBox;
        }).setHeader("On hands").setKey("bookOnHands");
        grid.setHeightByRows(true);
        grid.setItems(bookService.getAll());

        dialog.addOpenedChangeListener(dialogOpenedChangeEvent -> {
            if (!dialogOpenedChangeEvent.isOpened()) {
                grid.setItems(bookService.getAll());
            }
        });

        grid.asSingleSelect().addValueChangeListener(valueChangeListener -> {
            if (valueChangeListener.isFromClient()) {
                if (valueChangeListener.getValue() != null) {
                    book = valueChangeListener.getValue();
                } else {
                    book = valueChangeListener.getOldValue();
                }
                createNewDialog(book);
                dialog.open();
            }
        });
        newBookBtn = new Button("Add new book", clickEvent -> {
            createNewDialog(null);
            dialog.open();
        });
        add(newBookBtn, grid);
    }

    private void createNewDialog(Book book) {
        bookDialogView = bookDialogViewFactory.apply(book);
        dialog.removeAll();
        dialog.add(bookDialogView);
    }

}