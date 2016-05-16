package cleveroad.com.lib.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cleveroad.com.lib.R;
import cleveroad.com.lib.adapter.IOperationItem;
import cleveroad.com.lib.adapter.SimpleCategoriesAdapter;
import cleveroad.com.lib.model.MockedItemsFactory;
import cleveroad.com.lib.util.AbstractAnimatorListener;

public class EndlessNavigationView extends FrameLayout implements OnItemClickListener {
    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;
    public static final int SELECTION_GRAVITY_START = 0;
    public static final int SELECTION_GRAVITY_END = 1;
    private static final String TAG = EndlessNavigationView.class.getSimpleName();

    //outside params
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter;
    private List<OnItemClickListener> clickListeners = new ArrayList<>();
    private int colorCodeSelectionView;

    //view settings
    private Animator selectionInAnimator;
    private Animator selectionOutAnimator;
    private int selectionMargin;
    private IOrientationState orientationState;

    private int realHidedPosition = 0;
    private FrameLayout flContainerSelected;
    private RecyclerView rvCategories;
    private CategoriesAdapter.CategoriesHolder categoriesHolder;
    private CategoriesAdapter categoriesAdapter;

    private LinearLayoutManager linearLayoutManager;
    private AbstractSpacesItemDecoration spacesItemDecoration;
    private boolean skipNextOnLayout;
    private boolean isIndeterminateInitialized;

    private Integer itemWidth;
    private Integer itemHeight;

    private RecyclerView.OnScrollListener indeterminateOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (linearLayoutManager.findFirstVisibleItemPosition() == 0 || linearLayoutManager.findFirstVisibleItemPosition() == Integer.MAX_VALUE) {
                linearLayoutManager.scrollToPosition(Integer.MAX_VALUE / 2);
            }
        }
    };

    public EndlessNavigationView(Context context) {
        super(context);
        init(context, null);
    }

    public EndlessNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EndlessNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EndlessNavigationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void inflate(IOrientationState orientationState) {
        inflate(getContext(), orientationState.getLayoutId(), this);
        flContainerSelected = (FrameLayout) findViewById(R.id.flContainerSelected);
        rvCategories = (RecyclerView) findViewById(R.id.rvCategories);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        //read customization attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EndlessNavigationView);
        int colorCodeListBackground = a.getColor(R.styleable.EndlessNavigationView_listBackground,
                ContextCompat.getColor(getContext(), R.color.default_list_background));
        colorCodeSelectionView = a.getColor(R.styleable.EndlessNavigationView_selectionBackground,
                ContextCompat.getColor(getContext(), R.color.default_selection_view_background));
        int orientation = a.getInteger(R.styleable.EndlessNavigationView_orientation, ORIENTATION_HORIZONTAL);
        int selectionAnimatorInId = a.getResourceId(R.styleable.EndlessNavigationView_selectionInAnimation, R.animator.scale_restore);
        int selectionAnimatorOutId = a.getResourceId(R.styleable.EndlessNavigationView_selectionOutAnimation, R.animator.scale_small);
        @GravityAttr int selectionGravity = a.getInteger(R.styleable.EndlessNavigationView_selectionGravity, SELECTION_GRAVITY_START);
        selectionMargin = a.getDimensionPixelSize(R.styleable.EndlessNavigationView_selectionMargin, getResources().getDimensionPixelSize(R.dimen.margin_selected_view));
        a.recycle();
        selectionInAnimator = AnimatorInflater.loadAnimator(getContext(), selectionAnimatorInId);
        selectionOutAnimator = AnimatorInflater.loadAnimator(getContext(), selectionAnimatorOutId);

        //current view has two state : horizontal & vertical. State design pattern
        orientationState = getOrientationStateFromParam(orientation, selectionGravity);

        inflate(orientationState);

        //note that flContainerSelected should be in FrameLayout
        FrameLayout.LayoutParams params = (LayoutParams) flContainerSelected.getLayoutParams();
        params.gravity = orientationState.getSelectionGravity();
        orientationState.setSelectionMargin(selectionMargin, params);
        flContainerSelected.setLayoutParams(params);

        linearLayoutManager = orientationState.getLayoutManager(getContext());
        rvCategories.setLayoutManager(linearLayoutManager);

        rvCategories.setBackgroundColor(colorCodeListBackground);
        ColorDrawable colorDrawable = new NegativeMarginFixColorDrawable(colorCodeSelectionView);
        flContainerSelected.setBackground(colorDrawable);

        if (isInEditMode()) {
            setCategoriesAdapter(new SimpleCategoriesAdapter(MockedItemsFactory.getCategoryItemsUniq()));
        }
    }

    @SuppressWarnings("unchecked assigment")
    public void setCategoriesAdapter(@NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter) {
        this.inputAdapter = inputAdapter;
        this.categoriesAdapter = new CategoriesAdapter(inputAdapter);
        IOperationItem firstItem = categoriesAdapter.getItem(0);
        firstItem.setVisible(false);

        categoriesAdapter.setListener(this);
        rvCategories.setAdapter(categoriesAdapter);

        categoriesHolder = (CategoriesAdapter.CategoriesHolder) categoriesAdapter.createViewHolder(rvCategories, CategoriesAdapter.VIEW_TYPE_OTHER);
        //set first item to selectionView
        categoriesHolder.bindItemWildcardHelper(inputAdapter, 0);
        categoriesHolder.itemView.setBackgroundColor(colorCodeSelectionView);

        flContainerSelected.addView(categoriesHolder.itemView);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) categoriesHolder.itemView.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
    }

    /** add item click listener to this view*/
    public boolean addOnItemClickListener(OnItemClickListener itemClickListener) {
        return clickListeners.add(itemClickListener);
    }

    /** remove item click listener from this view*/
    public boolean removeOnItemClickListener(OnItemClickListener itemClickListener) {
        return clickListeners.remove(itemClickListener);
    }

    private void notifyItemClickListeners(int normalizedPosition) {
        for (OnItemClickListener itemClickListener : clickListeners) {
            itemClickListener.onItemClicked(normalizedPosition);
        }
    }

    /** change selection view inAnimator in runtime*/
    public void setSelectionInAnimator(Animator selectionInAnimator) {
        this.selectionInAnimator = selectionInAnimator;
    }

    /** change selection view outAnimator in runtime*/
    public void setSelectionOutAnimator(Animator selectionOutAnimator) {
        this.selectionOutAnimator = selectionOutAnimator;
    }

    //very big duct tape
    private int calcItemWidth() {
        if (itemWidth == null) {
            for (int i = 0; i < rvCategories.getChildCount(); i++) {
                itemWidth = rvCategories.getChildAt(i).getWidth();
                if (itemWidth != 0) break;
            }
        }
        return itemWidth;
    }

    private int calcItemHeight() {
        if (itemHeight == null) {
            for (int i = 0; i < rvCategories.getChildCount(); i++) {
                itemHeight = rvCategories.getChildAt(i).getHeight();
                if (itemHeight != 0) break;
            }
        }
        return itemHeight;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout");

        if (!skipNextOnLayout) {
            if (rvCategories.getChildCount() > 0) {
                int itemWidth = calcItemWidth();
                int itemHeight = calcItemHeight();

                boolean isFitOnScreen = orientationState.isItemsFitOnScreen(rvCategories.getWidth(),
                        rvCategories.getHeight(), itemWidth, itemHeight, categoriesAdapter.getWrappedItems().size());

                if (isFitOnScreen) {
                    rvCategories.removeItemDecoration(spacesItemDecoration);
                    Log.i(TAG, "all items fit on screen");
                    categoriesAdapter.setIndeterminate(false);
                    spacesItemDecoration = orientationState.getSelectionViewItemDecoration(selectionMargin,
                            categoriesHolder.itemView.getWidth(), categoriesHolder.itemView.getHeight());
                    rvCategories.addItemDecoration(spacesItemDecoration);
                    //changing item decoration will call onLayout again, so this flag needed to avoid indeterminate loop
                    skipNextOnLayout = true;
                    isIndeterminateInitialized = false;

                    rvCategories.removeOnScrollListener(indeterminateOnScrollListener);
                } else {
                    if (!isIndeterminateInitialized) {
                        //scroll to middle of indeterminate recycler view on initialization and if user somehow scrolled to start or end
                        linearLayoutManager.scrollToPositionWithOffset(Integer.MAX_VALUE / 2, getResources().getDimensionPixelOffset(R.dimen.selected_view_size_plus_margin));
                        rvCategories.addOnScrollListener(indeterminateOnScrollListener);
                        isIndeterminateInitialized = true;
                    }
                }
            }
        } else {
            skipNextOnLayout = false;
        }
    }

    private void startSelectedViewOutAnimation(int position) {
        Animator animator = selectionOutAnimator;
        animator.setTarget(categoriesHolder.itemView);
        animator.start();
        animator.addListener(new AbstractAnimatorListener() {
            @SuppressWarnings("unchecked assigment")
            @Override
            public void onAnimationEnd(Animator animation) {
                //replace selected view
                categoriesHolder.bindItemWildcardHelper(inputAdapter, position);
                startSelectedViewInAnimation();
            }
        });
    }

    private void startSelectedViewInAnimation() {
        Animator animator = selectionInAnimator;
        animator.setTarget(categoriesHolder.itemView);
        animator.start();
    }

    @Override
    public void onItemClicked(int position) {
        IOperationItem item = categoriesAdapter.getItem(position);
        IOperationItem oldHidedItem = categoriesAdapter.getItem(realHidedPosition);

        int realPosition = categoriesAdapter.normalizePosition(position);
        int itemToShowAdapterPosition = position - realPosition + realHidedPosition;

        item.setVisible(false);

        startSelectedViewOutAnimation(position);

        categoriesAdapter.notifyItemChanged(position);
        realHidedPosition = realPosition;

        oldHidedItem.setVisible(true);
        categoriesAdapter.notifyItemChanged(itemToShowAdapterPosition);

        notifyItemClickListeners(realPosition);

        Log.i(TAG, "clicked on position =" + position);
    }

    //orientation state factory method
    public IOrientationState getOrientationStateFromParam(int orientation, @GravityAttr int selectionGravityState) {
        return orientation == ORIENTATION_VERTICAL ? new OrientationStateVertical(selectionGravityState) : new OrientationStateHorizontal(selectionGravityState);
    }

    @IntDef({SELECTION_GRAVITY_START, SELECTION_GRAVITY_END})
    @interface GravityAttr {
    }

}
