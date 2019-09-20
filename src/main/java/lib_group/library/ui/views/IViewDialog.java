package lib_group.library.ui.views;

import lib_group.library.models.PublishingHouse;

public interface IViewDialog<T> {
    void setData(T obj);

    T getData();
}
