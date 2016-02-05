package cleveroad.com.lib.adapter;

public class OperationItem<T> implements IOperationItem<T>{
    private boolean isVisible = true;
    private T item;

    public OperationItem(T item){
        this.item = item;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    @Override
    public T getWrappedItem() {
        return item;
    }
}
