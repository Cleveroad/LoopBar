package cleveroad.com.lib.adapter;

public interface IOperationItem<T> {
    boolean isVisible();

    void setVisible(boolean isVisible);

    T getWrappedItem();
}
