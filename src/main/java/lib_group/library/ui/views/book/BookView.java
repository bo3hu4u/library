package lib_group.library.ui.views.book;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import lib_group.library.models.Book;
import lib_group.library.models.PublishingHouse;
import lib_group.library.services.interfaces.IBookService;
import lib_group.library.ui.views.IViewDialog;
import lib_group.library.ui.views.author.AuthorView;
import lib_group.library.ui.views.factories.ViewDialogFactory;
import lib_group.library.ui.views.home.HomeView;
import lib_group.library.ui.views.publishing_house.PublishingHouseView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Route("ui/books")
public class BookView extends VerticalLayout implements KeyNotifier {
    @Autowired
    private ViewDialogFactory dialogFactory;
    private Dialog dialog;
    private Grid<Book> grid;
    private Grid.Column<Book> publishingHousesColumn;
    private Book book;
    private IViewDialog<Book> bookDialogView;
    private Button newBookBtn;

    public BookView(IBookService bookService) {
        HorizontalLayout menu = new HorizontalLayout();
        menu.add(new RouterLink("Home Page", HomeView.class));
        menu.add(new RouterLink("Authors", AuthorView.class));
        menu.add(new RouterLink("Publishing Houses",
                PublishingHouseView.class));


        dialog = new Dialog();
        grid = new Grid<>();

        grid.addColumn(book -> book.getAuthor() != null ? book.getAuthor().getName() : "Not specified").setHeader("Author").setKey("author").setSortable(true);
        grid.addColumn(book -> book.getTitle()).setHeader("Title").setKey("title").setSortable(true);
        grid.addComponentColumn(book -> {
            Label description = new Label();
            description.getElement().getStyle().set("white-space", "pre-wrap").set("word-wrap", "break-word");
            if (book.getDescription() != null) {
                description.setText(book.getDescription().getBookDescription());
            } else {
                description.setText("No description");
            }
            return description;
        }).setHeader("Description").setKey("description").setFlexGrow(3).setSortable(true);
        grid.addColumn(book -> book.getEditionYear()).setHeader("Edition year").setKey("editionYear").setSortable(true);
        publishingHousesColumn = grid.addComponentColumn(book -> {
            VerticalLayout bookPublishingHouses = new VerticalLayout();
            if (book.getPublishingHouses().size() > 2) {
                bookPublishingHouses.setHeight("100px");
            }
            bookPublishingHouses.getElement().getStyle().set("overflow", "auto");
            book.getPublishingHouses().forEach(publishingHouse -> bookPublishingHouses.add(new Label(publishingHouse.getName())));
            return bookPublishingHouses;
        }).setHeader("Publishing houses").setKey("publishingHouse");
        publishingHousesColumn.setComparator(value -> {
            String pHNames = "";
            for (PublishingHouse ph : value.getPublishingHouses()) {
                pHNames = pHNames + ph.getName();
            }
            return pHNames;
        });
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
                            grid.setItems(bookService.getAll());
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
        }).setHeader("On hands").setKey("bookOnHands").setComparator(comparator -> comparator.isBookOnHands()).setSortable(true);

        setFiltersToColumns();
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
        add(menu, newBookBtn, grid);
    }

    private void setFiltersToColumns() {
        HeaderRow filterRow = grid.prependHeaderRow();
        TextField filterTitleTextField = new TextField();
        filterTitleTextField.addValueChangeListener(valueChangeEvent -> {
            ((ListDataProvider<Book>) grid.getDataProvider()).addFilter(book -> StringUtils.containsIgnoreCase(book.getTitle(), filterTitleTextField.getValue()));
        });
        filterTitleTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(grid.getColumnByKey("title")).setComponent(filterTitleTextField);


        Checkbox filterOnHands = new Checkbox();
        filterOnHands.addValueChangeListener(valueChangeEvent -> {
            ((ListDataProvider<Book>) grid.getDataProvider()).addFilter(book -> book.isBookOnHands().equals(filterOnHands.getValue()));
        });
        filterRow.getCell(grid.getColumnByKey("bookOnHands")).setComponent(filterOnHands);

    }

    private void createNewDialog(Book book) {
        bookDialogView = dialogFactory.getDialog("bookDialog");
        bookDialogView.setData(book);
        dialog.removeAll();
        dialog.add((BookDialogView) bookDialogView);
    }

}