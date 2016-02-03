package cleveroad.com.lib.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.LinkedList;

import cleveroad.com.lib.R;
import cleveroad.com.lib.adapter.CategoriesAdapter;
import static cleveroad.com.lib.adapter.CategoriesAdapter.ICategoryItem;
import cleveroad.com.lib.model.MockedItemsFactory;
import cleveroad.com.lib.util.AbstractAnimatorListener;

public class CategoriesEndlessNavigationView extends FrameLayout implements OnItemClickListener<CategoriesAdapter.ICategoryItem> {
    private static final String TAG = CategoriesEndlessNavigationView.class.getSimpleName();

    private FrameLayout flContainerSelected;
    private RecyclerView rvCategories;
    private CategoriesAdapter.CategoriesHolder categoriesHolder;
    private CategoriesAdapter categoriesAdapter;
    LinkedList<ICategoryItem> items;

    int realHidedPosition = 0;

    public CategoriesEndlessNavigationView(Context context) {
        super(context);
        init(context);
    }

    public CategoriesEndlessNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoriesEndlessNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CategoriesEndlessNavigationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            View view = inflate(context, R.layout.view_categories_navigation, this);
            flContainerSelected = (FrameLayout) view.findViewById(R.id.flContainerSelected);
            rvCategories = (RecyclerView) view.findViewById(R.id.rvCategories);

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            rvCategories.setLayoutManager(linearLayoutManager);

            items = MockedItemsFactory.getCategoryItemsUniq();
            items.get(0).setVisible(false);

            categoriesAdapter = new CategoriesAdapter(items, this);
            rvCategories.setAdapter(categoriesAdapter);

            View itemView = CategoriesAdapter.createView(flContainerSelected);
            categoriesHolder = CategoriesAdapter.CategoriesHolder.newBuilder(itemView).build();
            categoriesHolder.bindItem(items.get(0));
            flContainerSelected.addView(itemView);

            linearLayoutManager.scrollToPositionWithOffset(Integer.MAX_VALUE / 2, getResources().getDimensionPixelOffset(R.dimen.selected_view_size_plus_margin));

            rvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (linearLayoutManager.findFirstVisibleItemPosition() == 0 || linearLayoutManager.findFirstVisibleItemPosition() == Integer.MAX_VALUE) {
                        linearLayoutManager.scrollToPosition(Integer.MAX_VALUE/2);
                    }
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isInEditMode()) {
            LayoutParams params = (LayoutParams) rvCategories.getLayoutParams();
            params.height = categoriesHolder.itemView.getHeight();
            rvCategories.setLayoutParams(params);
        }
    }

    public Animator getSelectedViewInAnimator() {
        return AnimatorInflater.loadAnimator(getContext(), R.animator.scale_small);
    }

    public Animator getSelectedViewOutAnimator() {
        return AnimatorInflater.loadAnimator(getContext(), R.animator.scale_restore);
    }

    private void startSelectedViewInAnimation(final ICategoryItem item){
        Animator animator = getSelectedViewInAnimator();
        animator.setTarget(categoriesHolder.itemView);
        animator.start();
        animator.addListener(new AbstractAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //replace selected view
                categoriesHolder.bindItem(item);
                startSelectedViewOutAnimation();
            }
        });
    }

    private void startSelectedViewOutAnimation() {
        Animator animator = getSelectedViewOutAnimator();
        animator.setTarget(categoriesHolder.itemView);
        animator.start();
    }

    @Override
    public void onItemClicked(CategoriesAdapter.ICategoryItem item, int position) {
        ICategoryItem oldHidedItem = items.get(realHidedPosition);

        int realPosition = position % items.size();
        int itemToShowAdapterPosition = position - realPosition + realHidedPosition;

        item.setVisible(false);

        startSelectedViewInAnimation(item);

        categoriesAdapter.notifyItemChanged(position);
        realHidedPosition = realPosition;

        oldHidedItem.setVisible(true);
        categoriesAdapter.notifyItemChanged(itemToShowAdapterPosition);

        Log.i(TAG, "clicked on position =" + position);
    }
}
