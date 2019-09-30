package lib_group.library.ui.views.home;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import models.Book;
import lib_group.library.ui.views.IViewDialog;
import lib_group.library.ui.views.author.AuthorView;
import lib_group.library.ui.views.book.BookDialogView;
import lib_group.library.ui.views.book.BookView;
import lib_group.library.ui.views.factories.ViewDialogFactory;
import lib_group.library.ui.views.publishing_house.PublishingHouseView;

@Route("")
public class HomeView extends VerticalLayout {

    private final IViewDialog<Book> bookDialogView;
    private final HorizontalLayout hlControlButtons;

    public HomeView(ViewDialogFactory dialogFactory) {
        HorizontalLayout menu = new HorizontalLayout();
        menu.add(new RouterLink("Authors", AuthorView.class));
        menu.add(new RouterLink("Books", BookView.class));
        menu.add(new RouterLink("Publishing Houses",
                PublishingHouseView.class));
        add(menu);
        bookDialogView = dialogFactory.getDialog("bookDialog");
        hlControlButtons = (HorizontalLayout) bookDialogView.findComponentWithId((BookDialogView) bookDialogView, "hlControlButtons");
        hlControlButtons.setVisible(false);
        bookDialogView.setData(null);
        add((BookDialogView) bookDialogView);
    }
}
