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

    public static final int VIEW_TYPE_OFFSET = 1;

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
        return inputAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<IOperationItem> holder, int position) {
        if (!isIndeterminate && ((selectedGravity == LoopBarView.SELECTION_GRAVITY_START && position == 0)
                || (selectedGravity == LoopBarView.SELECTION_GRAVITY_END && position == getItemCount() - 1))) {
            holder.bindItem(null);
        } else {
            position = offsetPosition(position);
            inputAdapter.onBindViewHolder(holder, position);
        }
    }

    CategoriesAdapter getInnerAdapter() {
        return inputAdapter;
    }

    @Override
    public int getItemCount() {
        return isIndeterminate ? inputAdapter.getItemCount() : inputAdapter.getItemCount() + 1;
    }
//
//    @Override
//    public void onItemClicked(int position) {
//
//    }

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


    private int offsetPosition(int position) {
        return !isIndeterminate && selectedGravity == LoopBarView.SELECTION_GRAVITY_START ? position - 1 : position;
    }

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        inputAdapter.addOnItemClickListener(onItemClickListener);
    }

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

//    private IOperationItem getItemWithOffset(int position) {
//        position = normalizePosition(position);
//        return wrappedItems.get(position);
//    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        inputAdapter.setOrientation(orientation);

    }

    public int getSelectedGravity() {
        return selectedGravity;
    }

    void setSelectedGravity(int selectedGravity) {
        this.selectedGravity = selectedGravity;
        notifyDataSetChanged();
    }

    int normalizePosition(int position) {
        return inputAdapter.normalizePosition(position);
    }

//    private int normalizePositionOffset(int position) {
//        return isIndeterminate ? normalizePosition(position) : normalizePosition(position) - 1;
//    }
//
//    private int unNormalizePosition(int position) {
//        return isIndeterminate ? normalizePosition(position) : normalizePosition(position) + 1;
//    }

    private View createHeaderView(ViewGroup parent) {
        @LayoutRes int layoutId = orientation == Orientation.ORIENTATION_VERTICAL
                ? R.layout.enls_empty_header_vertical
                : R.layout.enls_empty_header_horizontal;
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    public void notifyItemChangedFix(int position) {
        position = offsetPosition(position);
        notifyItemChanged(position);
//        inputAdapter.notifyItemChanged(position);
    }

    static class HeaderHolder extends BaseRecyclerViewHolder<IOperationItem> {

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBindItem(IOperationItem item) {
            // do nothing
        }
    }
}
