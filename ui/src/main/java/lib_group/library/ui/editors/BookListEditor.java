<<<<<<< HEAD
package lib_group.library.ui.editors;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import models.Book;
import lib_group.library.ui.SelectionComboBoxListLayout;

import java.lang.reflect.Method;
import java.util.List;

@SpringComponent
@UIScope
public class BookListEditor extends SelectionComboBoxListLayout {
    public BookListEditor(List<Book> books) {
        super(books);
    }

}
=======
package lib_group.library.ui.editors;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import models.Book;
import lib_group.library.ui.SelectionComboBoxListLayout;

import java.util.List;

@SpringComponent
@UIScope
public class BookListEditor extends SelectionComboBoxListLayout {
    public BookListEditor(List<Book> books) {
        super(books);
    }
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
