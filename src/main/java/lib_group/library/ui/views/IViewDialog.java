package lib_group.library.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.ThemableLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lib_group.library.models.PublishingHouse;

import java.util.stream.Collectors;

public interface IViewDialog<T> {
    void setData(T obj);

    T getData();


    default Component findComponentWithId(Component root, String id) {
        for (Component child : root.getChildren().collect(Collectors.toList())) {
            if (id.equals(child.getId().get())) {
                return child;
            } else if (child.getChildren().count() > 0) {
                return findComponentWithId(child, id);
            }
        }
        return null;
    }


}
