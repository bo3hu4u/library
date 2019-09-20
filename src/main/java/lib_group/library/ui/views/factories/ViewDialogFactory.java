package lib_group.library.ui.views.factories;

import lib_group.library.ui.views.IViewDialog;

public interface ViewDialogFactory {
    IViewDialog getDialog(String name);
}
