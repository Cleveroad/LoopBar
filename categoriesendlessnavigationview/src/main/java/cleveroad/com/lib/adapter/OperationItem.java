package cleveroad.com.lib.adapter;

public class OperationItem implements IOperationItem{
    private boolean isVisible = true;
    private Object item;

    public OperationItem(Object item){
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
    public Object getWrappedItem() {
        return item;
    }
}
