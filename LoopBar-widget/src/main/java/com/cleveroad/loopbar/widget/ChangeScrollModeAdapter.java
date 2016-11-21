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


class ChangeScrollModeAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<IOperationItem>> {

    static final int VIEW_TYPE_CHANGE_SCROLL_MODE = 2;
    private static final int VIEW_TYPE_OFFSET = 1;
    @Orientation
    private int mOrientation = Orientation.ORIENTATION_VERTICAL;
    private CategoriesAdapter mInputAdapter;
    private boolean isIndeterminate = true;
    @LoopBarView.GravityAttr
    private int mSelectedGravity = LoopBarView.SELECTION_GRAVITY_START;

    ChangeScrollModeAdapter(@NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter) {
        mInputAdapter = new CategoriesAdapter(inputAdapter);
    }

    @Override
    public BaseRecyclerViewHolder<IOperationItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_OFFSET) {
            return new HeaderHolder(createHeaderView(parent));
        }
        if (viewType == VIEW_TYPE_CHANGE_SCROLL_MODE) {
            return new ChangeScrollModeHolder((CategoriesAdapter.CategoriesHolder) mInputAdapter.onCreateViewHolder(parent, CategoriesAdapter.VIEW_TYPE_OTHER));
        }
        return mInputAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<IOperationItem> holder, int position) {
        if ((mSelectedGravity == LoopBarView.SELECTION_GRAVITY_START && position == 0)
                || (mSelectedGravity == LoopBarView.SELECTION_GRAVITY_END && position == getItemCount() - 1))
            if (!isIndeterminate) {
                holder.bindItem(null, position);
            } else {
                mInputAdapter.onBindViewHolder(holder, offsetPosition(position));
            }
        else {
            mInputAdapter.onBindViewHolder(holder, offsetPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        return isIndeterminate ? mInputAdapter.getItemCount() : mInputAdapter.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        boolean needOffsetView = (mSelectedGravity == LoopBarView.SELECTION_GRAVITY_START && position == 0)
                || (mSelectedGravity == LoopBarView.SELECTION_GRAVITY_END && position == getItemCount() - 1);
        if (!isIndeterminate && needOffsetView) {
            return VIEW_TYPE_OFFSET;
        }
        return mInputAdapter.getItemViewType(offsetPosition(position));
    }


    private int offsetPosition(int position) {
        return !isIndeterminate && mSelectedGravity == LoopBarView.SELECTION_GRAVITY_START ? position - 1 : position;
    }

    private int unOffsetPosition(int position) {
        return !isIndeterminate && mSelectedGravity == LoopBarView.SELECTION_GRAVITY_START ? position + 1 : position;
    }

    void notifyRealItemChanged(int position) {
        notifyItemChanged(unOffsetPosition(position));
    }

    void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        mInputAdapter.addOnItemClickListener(onItemClickListener);
    }

    void removeOnItemClickListener(OnItemClickListener onItemClickListener) {
        mInputAdapter.removeOnItemClickListener(onItemClickListener);
    }

    Collection<IOperationItem> getWrappedItems() {
        return mInputAdapter.getWrappedItems();
    }


    void setListener(OnItemClickListener listener) {
        mInputAdapter.setListener(listener);
    }

    /**
     * Set mode for scrolling (Infinite or finite)
     *
     * @param isIndeterminate true for infinite
     */
    void setIndeterminate(boolean isIndeterminate) {
        this.isIndeterminate = isIndeterminate;
        mInputAdapter.setIndeterminate(isIndeterminate);
        notifyDataSetChanged();
    }

    IOperationItem getItem(int position) {
        return mInputAdapter.getItem(position);
    }


    void setOrientation(int orientation) {
        this.mOrientation = orientation;
        mInputAdapter.setOrientation(orientation);
    }

    int getSelectedGravity() {
        return mSelectedGravity;
    }

    void setSelectedGravity(int selectedGravity) {
        this.mSelectedGravity = selectedGravity;
        notifyDataSetChanged();
    }

    int normalizePosition(int position) {
        return mInputAdapter.normalizePosition(position);
    }


    private View createHeaderView(ViewGroup parent) {
        @LayoutRes int layoutId = mOrientation == Orientation.ORIENTATION_VERTICAL
                ? R.layout.enls_empty_header_vertical
                : R.layout.enls_empty_header_horizontal;
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @SuppressWarnings("WeakerAccess")
    class HeaderHolder extends BaseRecyclerViewHolder<IOperationItem> {

        HeaderHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBindItem(IOperationItem item, int position) {
            // do nothing
        }
    }

    // this class is only for encapsulation CategoriesHolder, actually now it doesn't change any logic
    class ChangeScrollModeHolder extends BaseRecyclerViewHolder<IOperationItem> {

        private CategoriesAdapter.CategoriesHolder categoriesHolder;

        ChangeScrollModeHolder(CategoriesAdapter.CategoriesHolder categoriesHolder) {
            super(categoriesHolder.itemView);
            this.categoriesHolder = categoriesHolder;
        }

        @Override
        protected void onBindItem(IOperationItem item, int position) {
            categoriesHolder.onBindItem(item, position);
        }

        <T extends RecyclerView.ViewHolder> void bindItemWildcardHelper(RecyclerView.Adapter<T> adapter, int position) {
            categoriesHolder.bindItemWildcardHelper(adapter, position);
        }
    }
}
