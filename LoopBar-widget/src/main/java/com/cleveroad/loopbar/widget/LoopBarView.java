package com.cleveroad.loopbar.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
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
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import com.cleveroad.loopbar.R;
import com.cleveroad.loopbar.adapter.IOperationItem;
import com.cleveroad.loopbar.adapter.SimpleCategoriesAdapter;
import com.cleveroad.loopbar.model.MockedItemsFactory;
import com.cleveroad.loopbar.util.AbstractAnimatorListener;

public class LoopBarView extends FrameLayout implements OnItemClickListener {
    public static final int SELECTION_GRAVITY_START = 0;
    public static final int SELECTION_GRAVITY_END = 1;
    private static final String TAG = LoopBarView.class.getSimpleName();

    //outside params
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter;
    private List<OnItemClickListener> clickListeners = new ArrayList<>();
    private int colorCodeSelectionView;

    //view settings
    private Animator selectionInAnimator;
    private Animator selectionOutAnimator;
    private int selectionMargin;
    private IOrientationState orientationState;
    private int placeHolderId;
    private int overlaySize;
    //state settings below
    private int currentItemPosition;
    @GravityAttr
    private int selectionGravity;

    private int realHidedPosition = 0;

    //views
    private FrameLayout flContainerSelected;
    private RecyclerView rvCategories;
    @Nullable
    private View overlayPlaceholder;
    private View viewColorable;

    private CategoriesAdapter.CategoriesHolder categoriesHolder;
    private CategoriesAdapter categoriesAdapter;

    private LinearLayoutManager linearLayoutManager;
    private AbstractSpacesItemDecoration spacesItemDecoration;
    private boolean skipNextOnLayout;
    private boolean isIndeterminateInitialized;

    private RecyclerView.OnScrollListener indeterminateOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (linearLayoutManager.findFirstVisibleItemPosition() == 0 || linearLayoutManager.findFirstVisibleItemPosition() == Integer.MAX_VALUE) {
                linearLayoutManager.scrollToPosition(Integer.MAX_VALUE / 2);
            }
        }
    };

    public LoopBarView(Context context) {
        super(context);
        init(context, null);
    }

    public LoopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoopBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void inflate(IOrientationState orientationState, int placeHolderId) {
        inflate(getContext(), orientationState.getLayoutId(), this);
        flContainerSelected = (FrameLayout) findViewById(R.id.flContainerSelected);
        rvCategories = (RecyclerView) findViewById(R.id.rvCategories);
        overlayPlaceholder = getRootView().findViewById(placeHolderId);
        viewColorable = getRootView().findViewById(R.id.viewColorable);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        //read customization attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopBarView);
        colorCodeSelectionView = a.getColor(R.styleable.LoopBarView_enls_selectionBackground,
                ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark));
        int orientation = a.getInteger(R.styleable.LoopBarView_enls_orientation, Orientation.ORIENTATION_HORIZONTAL);
        int selectionAnimatorInId = a.getResourceId(R.styleable.LoopBarView_enls_selectionInAnimation, R.animator.enls_scale_restore);
        int selectionAnimatorOutId = a.getResourceId(R.styleable.LoopBarView_enls_selectionOutAnimation, R.animator.enls_scale_small);
        placeHolderId = a.getResourceId(R.styleable.LoopBarView_enls_placeholderId, -1);
        @GravityAttr int selectionGravity = a.getInteger(R.styleable.LoopBarView_enls_selectionGravity, SELECTION_GRAVITY_START);

        this.selectionGravity = selectionGravity;

        selectionMargin = a.getDimensionPixelSize(R.styleable.LoopBarView_enls_selectionMargin,
                getResources().getDimensionPixelSize(R.dimen.enls_margin_selected_view));
        overlaySize = a.getDimensionPixelSize(R.styleable.LoopBarView_enls_overlaySize, 0);
        a.recycle();

        //check attributes you need, for example all paddings
        int [] attributes = new int [] {android.R.attr.background};
        //then obtain typed array
        a = context.obtainStyledAttributes(attrs, attributes);
        int backgroundResource = a.getResourceId(0, R.color.enls_default_list_background);
        a.recycle();

        selectionInAnimator = AnimatorInflater.loadAnimator(getContext(), selectionAnimatorInId);
        selectionOutAnimator = AnimatorInflater.loadAnimator(getContext(), selectionAnimatorOutId);

        //current view has two state : horizontal & vertical. State design pattern
        orientationState = getOrientationStateFromParam(orientation);
        inflate(orientationState, placeHolderId);
        setBackgroundResource(backgroundResource);
        setGravity(selectionGravity);

        ColorDrawable colorDrawable = new NegativeMarginFixColorDrawable(colorCodeSelectionView);
        viewColorable.setBackground(colorDrawable);

        linearLayoutManager = orientationState.getLayoutManager(getContext());
        rvCategories.setLayoutManager(linearLayoutManager);

        if (isInEditMode()) {
            setCategoriesAdapter(new SimpleCategoriesAdapter(MockedItemsFactory.getCategoryItemsUniq()));
        }
    }

    public void setGravity(@GravityAttr int selectionGravity) {
        orientationState.setSelectionGravity(selectionGravity);
        //note that flContainerSelected should be in FrameLayout
        FrameLayout.LayoutParams params = (LayoutParams) flContainerSelected.getLayoutParams();
        params.gravity = orientationState.getSelectionGravity();
        orientationState.setSelectionMargin(selectionMargin, params);
        flContainerSelected.setLayoutParams(params);
        this.selectionGravity = selectionGravity;
        invalidate();
    }

    @SuppressWarnings("unchecked assigment")
    public void setCategoriesAdapter(@NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter) {
        this.inputAdapter = inputAdapter;
        this.categoriesAdapter = new CategoriesAdapter(inputAdapter);
        IOperationItem firstItem = categoriesAdapter.getItem(0);
        firstItem.setVisible(false);

        categoriesAdapter.setListener(this);
        categoriesAdapter.setOrientation(orientationState.getOrientation());
        rvCategories.setAdapter(categoriesAdapter);

        categoriesHolder = (CategoriesAdapter.CategoriesHolder) categoriesAdapter.createViewHolder(rvCategories, CategoriesAdapter.VIEW_TYPE_OTHER);
        //set first item to selectionView
        categoriesHolder.bindItemWildcardHelper(inputAdapter, 0);
        categoriesHolder.itemView.setBackgroundColor(colorCodeSelectionView);

        flContainerSelected.addView(categoriesHolder.itemView);

        orientationState.initSelectionContainer(flContainerSelected);

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

    public RecyclerView getWrappedRecyclerView() {
        return rvCategories;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        overlayPlaceholder = ((ViewGroup)getParent()).findViewById(placeHolderId);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentItem(ss.currentItemPosition);
        setGravity(ss.selectionGravity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (!skipNextOnLayout) {

            if (overlaySize > 0 && overlayPlaceholder == null) {
                Log.e(TAG, "You have to add placeholder and set it id with #enls_placeHolderId parameter to use overlaySize");
            }

            orientationState.initPlaceHolderAndOverlay(overlayPlaceholder, rvCategories, overlaySize);

            if (rvCategories.getChildCount() > 0) {

                /** true if current set of category items fit on screen, so view shouldn't be indeterminate */
                boolean isFitOnScreen = orientationState.isItemsFitOnScreen(rvCategories, categoriesAdapter.getWrappedItems().size());

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
                        linearLayoutManager.scrollToPositionWithOffset(Integer.MAX_VALUE / 2, getResources().getDimensionPixelOffset(R.dimen.enls_selected_view_size_plus_margin));
                        rvCategories.addOnScrollListener(indeterminateOnScrollListener);
                        isIndeterminateInitialized = true;
                    }
                }
            }

            skipNextOnLayout = true;
        }
    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState,
                currentItemPosition, selectionGravity);
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

    /** set selected item in endless view. OnItemSelected listeners won't be invoked
     * @param currentItemPosition selected position*/
    public void setCurrentItem(int currentItemPosition) {
        selectItem(currentItemPosition, false);
    }

    /** set selected item in endless view.
     * OnItemSelected listeners won't be invoked
     * @param currentItemPosition selected position
     * @param isInvokeListeners should view notify OnItemSelected listeners about this selection*/
    public void setCurrentItem(int currentItemPosition, boolean isInvokeListeners) {
        selectItem(currentItemPosition, isInvokeListeners);
    }

    public void selectItem(int position, boolean invokeListeners) {
        IOperationItem item = categoriesAdapter.getItem(position);
        IOperationItem oldHidedItem = categoriesAdapter.getItem(realHidedPosition);

        int realPosition = categoriesAdapter.normalizePosition(position);
        //do nothing if position not changed
        if (realPosition == currentItemPosition) return;
        int itemToShowAdapterPosition = position - realPosition + realHidedPosition;

        item.setVisible(false);

        startSelectedViewOutAnimation(position);

        categoriesAdapter.notifyItemChanged(position);
        realHidedPosition = realPosition;

        oldHidedItem.setVisible(true);
        flContainerSelected.requestLayout();
        categoriesAdapter.notifyItemChanged(itemToShowAdapterPosition);

        this.currentItemPosition = realPosition;

        if (invokeListeners) {
            notifyItemClickListeners(realPosition);
        }

        Log.i(TAG, "clicked on position =" + position);
    }

    @Override
    public void onItemClicked(int position) {
        selectItem(position, true);
    }

    //orientation state factory method
    public IOrientationState getOrientationStateFromParam(int orientation) {
        return orientation == Orientation.ORIENTATION_VERTICAL ? new OrientationStateVertical() : new OrientationStateHorizontal();
    }

    @IntDef({SELECTION_GRAVITY_START, SELECTION_GRAVITY_END})
    public @interface GravityAttr {
    }

    public static class SavedState extends BaseSavedState implements Parcelable{

        public int currentItemPosition;
        @GravityAttr
        private int selectionGravity;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public int describeContents() {
            return 0;
        }

        // упаковываем объект в Parcel
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeInt(currentItemPosition);
            parcel.writeInt(selectionGravity);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            // распаковываем объект из Parcel
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        // конструктор, считывающий данные из Parcel
        private SavedState(Parcel parcel) {
            super(parcel);
            currentItemPosition = parcel.readInt();
            @GravityAttr
            int selectionGravity = parcel.readInt();
            this.selectionGravity = selectionGravity;
        }

        public SavedState(Parcelable superState, int currentItemPosition, int selectionGravity) {
            super(superState);
            this.currentItemPosition = currentItemPosition;
            this.selectionGravity = selectionGravity;
        }
    }

}
