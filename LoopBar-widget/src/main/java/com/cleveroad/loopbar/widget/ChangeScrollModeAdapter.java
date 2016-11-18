package com.cleveroad.loopbar.widget;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.loopbar.R;
import com.cleveroad.loopbar.adapter.IOperationItem;

import java.util.Collection;


public class ChangeScrollModeAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<IOperationItem>> {

    public static final int VIEW_TYPE_CHANGE_SCROLL_MODE = 2;
    private static final int VIEW_TYPE_OFFSET = 1;
    @Orientation
    private int orientation = Orientation.ORIENTATION_VERTICAL;
    private CategoriesAdapter inputAdapter;
    private boolean isIndeterminate = true;
    @LoopBarView.GravityAttr
    private int selectedGravity = LoopBarView.SELECTION_GRAVITY_START;

    public ChangeScrollModeAdapter(@NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter) {
        this.inputAdapter = new CategoriesAdapter(inputAdapter);
    }

    @Override
    public BaseRecyclerViewHolder<IOperationItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_OFFSET) {
            return new HeaderHolder(createHeaderView(parent));
        }
        if (viewType == VIEW_TYPE_CHANGE_SCROLL_MODE) {
            return new ChangeScrollModeHolder((CategoriesAdapter.CategoriesHolder) inputAdapter.onCreateViewHolder(parent, CategoriesAdapter.VIEW_TYPE_OTHER));
        }
        return inputAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<IOperationItem> holder, int position) {
        if (!isIndeterminate && ((selectedGravity == LoopBarView.SELECTION_GRAVITY_START && position == 0)
                || (selectedGravity == LoopBarView.SELECTION_GRAVITY_END && position == getItemCount() - 1))) {
            holder.bindItem(null, position);
        } else {
            position = offsetPosition(position);
            inputAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return isIndeterminate ? inputAdapter.getItemCount() : inputAdapter.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isIndeterminate &&
                ((selectedGravity == LoopBarView.SELECTION_GRAVITY_START && position == 0)
                        || (selectedGravity == LoopBarView.SELECTION_GRAVITY_END && position == getItemCount() - 1))) {
            return VIEW_TYPE_OFFSET;
        }
        position = offsetPosition(position);
        return inputAdapter.getItemViewType(position);
    }


    int offsetPosition(int position) {
        return !isIndeterminate && selectedGravity == LoopBarView.SELECTION_GRAVITY_START ? position - 1 : position;
    }

    int unOffsetPosition(int position) {
        return !isIndeterminate && selectedGravity == LoopBarView.SELECTION_GRAVITY_START ? position + 1 : position;
    }

    // TODO: 18.11.16 update
    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        inputAdapter.addOnItemClickListener(onItemClickListener);
    }

    // TODO: 18.11.16 update
    public void removeOnItemClickListener(OnItemClickListener onItemClickListener) {
        inputAdapter.removeOnItemClickListener(onItemClickListener);
    }


    public Collection<IOperationItem> getWrappedItems() {
        return inputAdapter.getWrappedItems();
    }

    public void setListener(OnItemClickListener listener) {
        inputAdapter.setListener(listener);
    }

    /**
     * Set mode for scrolling (Infinite or finite)
     *
     * @param isIndeterminate true for infinite
     */
    public void setIndeterminate(boolean isIndeterminate) {
        this.isIndeterminate = isIndeterminate;
        inputAdapter.setIndeterminate(isIndeterminate);
        notifyDataSetChanged();
    }

    public IOperationItem getItem(int position) {
        return inputAdapter.getItem(position);
    }


    void setOrientation(int orientation) {
        this.orientation = orientation;
        inputAdapter.setOrientation(orientation);
    }

    int getSelectedGravity() {
        return selectedGravity;
    }

    void setSelectedGravity(int selectedGravity) {
        this.selectedGravity = selectedGravity;
        notifyDataSetChanged();
    }

    int normalizePosition(int position) {
        return inputAdapter.normalizePosition(position);
    }


    private View createHeaderView(ViewGroup parent) {
        @LayoutRes int layoutId = orientation == Orientation.ORIENTATION_VERTICAL
                ? R.layout.enls_empty_header_vertical
                : R.layout.enls_empty_header_horizontal;
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    class HeaderHolder extends BaseRecyclerViewHolder<IOperationItem> {

        HeaderHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBindItem(IOperationItem item, int position) {
            // do nothing
        }
    }

    // this class is only for encapsulation CategoriesHolder
    public class ChangeScrollModeHolder extends BaseRecyclerViewHolder<IOperationItem> {

        private CategoriesAdapter.CategoriesHolder categoriesHolder;

        public ChangeScrollModeHolder(CategoriesAdapter.CategoriesHolder categoriesHolder) {
            super(categoriesHolder.itemView);
            this.categoriesHolder = categoriesHolder;
        }

        @Override
        protected void onBindItem(IOperationItem item, int position) {
            categoriesHolder.onBindItem(item, position);
        }

        public <T extends RecyclerView.ViewHolder> void bindItemWildcardHelper(RecyclerView.Adapter<T> adapter, int position) {
            categoriesHolder.bindItemWildcardHelper(adapter, position);
        }
    }
}
