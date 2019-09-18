package lib_group.library.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class SelectionComboBoxListLayout<T> extends VerticalLayout {
    private List<T> selectedObjList;
    private List<T> allObjects;
    private Button addObjBtn;

    public SelectionComboBoxListLayout(List<T> allObjects) {
        this.allObjects = allObjects;
        selectedObjList = new ArrayList<>();
        addObjBtn = new Button();
        addDetachListener(detachEvent -> selectedObjList.clear());
        setPadding(false);
        setMargin(false);
        add(addObjBtn);
        addObjBtn.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE_O));
        addObjBtn.addClickListener(clickEvent -> {
            createObjComboBox();
        });
    }

    public void setComboBoxesForObjects(List<T> objects) {
        for (T obj : objects) {
            createObjComboBox().setValue(obj);
        }
    }

    public Set<T> getSelectedObjects() {
        return new HashSet<>(selectedObjList);
    }

    private ComboBox<T> createObjComboBox() {
        HorizontalLayout editorHlObj = new HorizontalLayout();
        editorHlObj.setSizeFull();
        ComboBox<T> objectComboBox = new ComboBox<>();
        objectComboBox.addValueChangeListener(listener -> {
            selectedObjList.remove(listener.getOldValue());
            if (listener.getValue() != null) {
                selectedObjList.add(listener.getValue());
            }
        });
        objectComboBox.setSizeFull();
        objectComboBox.setItemLabelGenerator(object -> object.toString());
        objectComboBox.setItems(allObjects);
        Button removeObj = new Button();
        removeObj.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        removeObj.addClickListener(e -> {
            selectedObjList.remove(objectComboBox.getValue());
            remove(editorHlObj);
        });
        editorHlObj.add(objectComboBox, removeObj);
        add(editorHlObj);
        add(addObjBtn);
        return objectComboBox;
    }

}
