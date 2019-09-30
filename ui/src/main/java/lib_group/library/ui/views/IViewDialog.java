<<<<<<< HEAD
package lib_group.library.ui.views;

import com.vaadin.flow.component.Component;

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
=======
package lib_group.library.ui.views;

import com.vaadin.flow.component.Component;

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
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
