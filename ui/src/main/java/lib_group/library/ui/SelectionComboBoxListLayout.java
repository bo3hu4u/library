package lib_group.library.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class SelectionComboBoxListLayout<T> extends VerticalLayout {
    private List<T> selectedObjList;
    private List<T> allObjects;
    private List<ComboBox<T>> comboBoxList;
    private Button addObjBtn;

    public SelectionComboBoxListLayout(List<T> allObjects) {
        this.allObjects = allObjects;
        comboBoxList = new ArrayList<>();
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

    public void refreshComboBoxListItems() {
        comboBoxList.forEach(comboBox -> comboBox.getDataProvider().refreshAll());
    }

    public Set<T> getSelectedObjects() {
        return new HashSet<>(selectedObjList);
    }

    private ComboBox<T> createObjComboBox() {
        HorizontalLayout editorHlObj = new HorizontalLayout();
        editorHlObj.setSizeFull();
        ComboBox<T> objectComboBox = new ComboBox<>();
        objectComboBox.getDataProvider().addDataProviderListener(dataChangeEvent -> dataChangeEvent.getSource().refreshAll());
        comboBoxList.add(objectComboBox);
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
