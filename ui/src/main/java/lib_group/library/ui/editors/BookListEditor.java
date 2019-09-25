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
