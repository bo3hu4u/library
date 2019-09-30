package lib_group.library.ui.editors;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import models.PublishingHouse;
import lib_group.library.ui.SelectionComboBoxListLayout;

import java.util.List;

@SpringComponent
@UIScope
public class PublishingHouseListEditor extends SelectionComboBoxListLayout {
    public PublishingHouseListEditor(List<PublishingHouse> publishingHouses) {
        super(publishingHouses);
    }
}