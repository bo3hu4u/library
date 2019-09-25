package lib_group.library.ui.views.factories;

import lib_group.library.ui.views.IViewDialog;
import org.springframework.stereotype.Component;

@Component
public interface ViewDialogFactory {
    IViewDialog getDialog(String name);
}
