package lib_group.library.ui.views.publishing_house;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import models.PublishingHouse;
import services.interfaces.IPublishingHouseService;
import lib_group.library.ui.views.author.AuthorView;
import lib_group.library.ui.views.book.BookView;
import lib_group.library.ui.views.factories.ViewDialogFactory;
import lib_group.library.ui.views.home.HomeView;
import org.springframework.beans.factory.annotation.Autowired;

@Route("ui/publishing_houses")
public class PublishingHouseView extends VerticalLayout {
    private PublishingHouseDialogView publishingHouseDialogView;

    private Dialog dialog;
    private Grid<PublishingHouse> grid;
    private PublishingHouse publishingHouse;
    private Button newPublishingHouseBtn;
    @Autowired
    private ViewDialogFactory dialogFactory;

    public PublishingHouseView(IPublishingHouseService publishingHouseService) {
        HorizontalLayout menu = new HorizontalLayout();
        menu.add(new RouterLink("Home Page", HomeView.class));
        menu.add(new RouterLink("Books", BookView.class));
        menu.add(new RouterLink("Authors",
                AuthorView.class));

        dialog = new Dialog();
        grid = new Grid<>();

        grid.setHeightByRows(true);
        setWidth("60%");
        getElement().getStyle().set("margin", "auto");
        grid.addColumn(publishingHouse -> publishingHouse.getName()).setHeader("Name").setKey("publishingHouseName").setWidth("30%");
        grid.addColumn(publishingHouse -> publishingHouse.getLocation() != null ? publishingHouse.getLocation().getAddress() : "No address")
                .setHeader("Location").setKey("publishingHouseLocation").setWidth("20%");
        grid.addComponentColumn(publishingHouse -> {
            VerticalLayout booksByPublishingHouse = new VerticalLayout();
            if (publishingHouse.getBooks().size() > 2) {
                booksByPublishingHouse.setHeight("100px");
            }
            booksByPublishingHouse.getElement().getStyle().set("overflow", "auto");
            publishingHouse.getBooks().forEach(book -> booksByPublishingHouse.add(new Label(book.getTitle())));
            return booksByPublishingHouse;
        }).setHeader("Books").setKey("books").setWidth("50%");

        grid.setItems(publishingHouseService.getAll());

        dialog.addOpenedChangeListener(dialogOpenedChangeEvent -> {
            if (!dialogOpenedChangeEvent.isOpened()) {
                grid.setItems(publishingHouseService.getAll());
            }
        });

        grid.asSingleSelect().addValueChangeListener(valueChangeListener -> {
            if (valueChangeListener.isFromClient()) {
                if (valueChangeListener.getValue() != null) {
                    publishingHouse = valueChangeListener.getValue();
                } else {
                    publishingHouse = valueChangeListener.getOldValue();
                }
                createNewDialog(publishingHouse);
                dialog.open();
            }
        });

        newPublishingHouseBtn = new Button("Add new publishing house", clickEvent -> {
            createNewDialog(null);
            dialog.open();
        });
        add(menu, newPublishingHouseBtn, grid);
    }

    private void createNewDialog(PublishingHouse publishingHouse) {
        publishingHouseDialogView = (PublishingHouseDialogView) dialogFactory.getDialog("publishingHouseDialog");
        publishingHouseDialogView.setData(publishingHouse);
        dialog.removeAll();
        dialog.add(publishingHouseDialogView);
    }

}