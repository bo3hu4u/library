package lib_group.library.ui.views.author;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import lib_group.library.models.Author;
import lib_group.library.services.interfaces.IAuthorService;
import lib_group.library.ui.views.IViewDialog;
import lib_group.library.ui.views.book.BookView;
import lib_group.library.ui.views.factories.ViewDialogFactory;
import lib_group.library.ui.views.home.HomeView;
import lib_group.library.ui.views.publishing_house.PublishingHouseView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;

@Route("ui/authors")
public class AuthorView extends VerticalLayout {
    @Autowired
    private ViewDialogFactory dialogFactory;

    private Dialog dialog;
    private Grid<Author> grid;
    private Author author;
    private AuthorDialogView authorDialogView;
    private Button newAuthorBtn;

    public AuthorView(IAuthorService authorService) {
        HorizontalLayout menu = new HorizontalLayout();
        menu.add(new RouterLink("Home Page", HomeView.class));
        menu.add(new RouterLink("Books", BookView.class));
        menu.add(new RouterLink("Publishing Houses",
                PublishingHouseView.class));

        dialog = new Dialog();
        grid = new Grid<>();

        grid.setHeightByRows(true);
        setWidth("60%");
        getElement().getStyle().set("margin", "auto");
        grid.addColumn(author -> author.getName()).setHeader("Name").setKey("authorName").setWidth("30%");
        grid.addColumn(author -> author.getBirthYear()).setHeader("Birth year").setKey("authorBirthYear").setWidth("20%");
        grid.addComponentColumn(author -> {
            VerticalLayout booksByAuthor = new VerticalLayout();
            if (author.getBooks().size() > 2) {
                booksByAuthor.setHeight("100px");
            }
            booksByAuthor.getElement().getStyle().set("overflow", "auto");
            author.getBooks().forEach(book -> booksByAuthor.add(new Label(book.getTitle())));
            return booksByAuthor;
        }).setHeader("Books").setKey("books").setWidth("50%");
        grid.setItems(authorService.getAll());

        dialog.addOpenedChangeListener(dialogOpenedChangeEvent -> {
            if (!dialogOpenedChangeEvent.isOpened()) {
                grid.setItems(authorService.getAll());
            }
        });

        grid.asSingleSelect().addValueChangeListener(valueChangeListener -> {
            if (valueChangeListener.isFromClient()) {
                if (valueChangeListener.getValue() != null) {
                    author = valueChangeListener.getValue();
                } else {
                    author = valueChangeListener.getOldValue();
                }
                createNewDialog(author);
                dialog.open();
            }
        });

        newAuthorBtn = new Button("Add new author", clickEvent -> {
            createNewDialog(null);
            dialog.open();
        });


        add(menu, newAuthorBtn, grid);
    }

    private void createNewDialog(Author author) {
        authorDialogView = (AuthorDialogView) dialogFactory.getDialog("authorDialog");
        authorDialogView.setData(author);
        dialog.removeAll();
        dialog.add(authorDialogView);
    }

}