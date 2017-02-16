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
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.cleveroad.loopbar.BuildConfig;
import com.cleveroad.loopbar.R;
import com.cleveroad.loopbar.adapter.ICategoryItem;
import com.cleveroad.loopbar.adapter.ILoopBarPagerAdapter;
import com.cleveroad.loopbar.adapter.IOperationItem;
import com.cleveroad.loopbar.adapter.SimpleCategoriesAdapter;
import com.cleveroad.loopbar.adapter.SimpleCategoriesMenuAdapter;
import com.cleveroad.loopbar.model.CategoryItem;
import com.cleveroad.loopbar.model.MockedItemsFactory;
import com.cleveroad.loopbar.util.AbstractAnimatorListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LoopBarView extends FrameLayout implements OnItemClickListener {

    /**
     * Gravity constant for selector.
     * Representing state of selector attached to the left if LoopBar is horizontal
     * and to the top if LoopBar is vertical
     *
     * @see GravityAttr
     * @see #setGravity(int)
     * @see #getGravity()
     */
    public static final int SELECTION_GRAVITY_START = 0;

    /**
     * Gravity constant for selector.
     * Representing state of selector attached  to the right if LoopBar is horizontal
     * and to the bottom if LoopBar is vertical
     *
     * @see GravityAttr
     * @see #setGravity(int)
     * @see #getGravity()
     */
    public static final int SELECTION_GRAVITY_END = 1;

    /**
     * Scroll mode constant for LoopBar
     * Representing automatic (adapting) scrolling state of LoopBar
     * If amount of items in LoopBar won't be enough to get out of bounds of LoopBar
     * (i.e. all items fit on screen) it will have finite behavior {@link #SCROLL_MODE_FINITE}.
     * In another case there will be infinite behavior {@link #SCROLL_MODE_INFINITE}
     * (there will be displayed only one appearance of each added item in LoopBar)
     *
     * @see ScrollAttr
     * @see #setScrollMode(int)
     * @see #getScrollMode()
     * @see #SCROLL_MODE_FINITE
     * @see #SCROLL_MODE_INFINITE
     */
    public static final int SCROLL_MODE_AUTO = 2;

    /**
     * Scroll mode constant for LoopBar
     * Representing infinite scrolling state of LoopBar
     * (items will repeatedly show in the LoopBar while you scroll it)
     *
     * @see ScrollAttr
     * @see #setScrollMode(int)
     * @see #getScrollMode()
     */
    public static final int SCROLL_MODE_INFINITE = 3;

    /**
     * Scroll mode constant for LoopBar
     * Representing finite scrolling state of LoopBar
     * (there will be displayed only one appearance of each added item in LoopBar)
     *
     * @see ScrollAttr
     * @see #setScrollMode(int)
     * @see #getScrollMode()
     */
    public static final int SCROLL_MODE_FINITE = 4;

    private static final String TAG = LoopBarView.class.getSimpleName();

    //outside params
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mInputAdapter;
    private List<OnItemClickListener> mClickListeners = new ArrayList<>();
    private int mColorCodeSelectionView;

    //view settings
    private Animator mSelectionInAnimator;
    private Animator mSelectionOutAnimator;
    private int mSelectionMargin;
    private IOrientationState mOrientationState;
    private int mPlaceHolderId;
    private int mOverlaySize;
    //state settings below
    private int mCurrentItemPosition;
    @GravityAttr
    private int mSelectionGravity;

    private int mRealHidedPosition = 0;

    //views
    private FrameLayout mFlContainerSelected;
    private RecyclerView mRvCategories;
    @Nullable
    private View mOverlayPlaceholder;
    private View mViewColorable;

    private ChangeScrollModeAdapter.ChangeScrollModeHolder mSelectorHolder;
    private ChangeScrollModeAdapter mOuterAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private boolean mSkipNextOnLayout;
    private boolean mIndeterminateInitialized;
    private boolean mInfinite;

    @ScrollAttr
    private int mScrollMode;

    private IndeterminateOnScrollListener mIndeterminateOnScrollListener = new IndeterminateOnScrollListener(this);

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

    private void inflate(IOrientationState orientationState, int placeHolderId, int backgroundResource) {
        inflate(getContext(), orientationState.getLayoutId(), this);
        mFlContainerSelected = (FrameLayout) findViewById(R.id.flContainerSelected);
        mRvCategories = (RecyclerView) findViewById(R.id.rvCategories);
        View vRvContainer = findViewById(R.id.vRvContainer);
        mOverlayPlaceholder = getRootView().findViewById(placeHolderId);
        mViewColorable = getRootView().findViewById(R.id.viewColorable);
        /* background color must be set to container of recyclerView.
         * If you set it to main view, there will be any transparent part
         * when selector has overlay */
        vRvContainer.setBackgroundResource(backgroundResource);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        //read customization attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoopBarView);
        mColorCodeSelectionView = typedArray.getColor(R.styleable.LoopBarView_enls_selectionBackground,
                ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark));
        int orientation = typedArray
                .getInteger(R.styleable.LoopBarView_enls_orientation, Orientation.ORIENTATION_HORIZONTAL);
        int selectionAnimatorInId = typedArray
                .getResourceId(R.styleable.LoopBarView_enls_selectionInAnimation, R.animator.enls_scale_restore);
        int selectionAnimatorOutId = typedArray
                .getResourceId(R.styleable.LoopBarView_enls_selectionOutAnimation, R.animator.enls_scale_small);
        mPlaceHolderId = typedArray.getResourceId(R.styleable.LoopBarView_enls_placeholderId, -1);
        @GravityAttr int selectionGravity = typedArray
                .getInteger(R.styleable.LoopBarView_enls_selectionGravity, SELECTION_GRAVITY_START);
        mSelectionGravity = selectionGravity;
        mInfinite = typedArray.getBoolean(R.styleable.LoopBarView_enls_infiniteScrolling, true);

        @ScrollAttr int scrollMode = typedArray.getInt(R.styleable.LoopBarView_enls_scrollMode,
                mInfinite ? SCROLL_MODE_INFINITE : SCROLL_MODE_FINITE);
        mScrollMode = scrollMode;


        mSelectionMargin = typedArray.getDimensionPixelSize(R.styleable.LoopBarView_enls_selectionMargin,
                getResources().getDimensionPixelSize(R.dimen.enls_margin_selected_view));
        mOverlaySize = typedArray.getDimensionPixelSize(R.styleable.LoopBarView_enls_overlaySize, 0);
        typedArray.recycle();

        //check attributes you need, for example all paddings
        int[] attributes = new int[]{android.R.attr.background};
        //then obtain typed array
        typedArray = context.obtainStyledAttributes(attrs, attributes);
        int backgroundResource = typedArray.getResourceId(0, R.color.enls_default_list_background);

        mSelectionInAnimator = AnimatorInflater.loadAnimator(getContext(), selectionAnimatorInId);
        mSelectionOutAnimator = AnimatorInflater.loadAnimator(getContext(), selectionAnimatorOutId);

        //current view has two state : horizontal & vertical. State design pattern
        mOrientationState = getOrientationStateFromParam(orientation);
        inflate(mOrientationState, mPlaceHolderId, backgroundResource);
        setGravity(selectionGravity);

        ColorDrawable colorDrawable = new NegativeMarginFixColorDrawable(mColorCodeSelectionView);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mViewColorable.setBackgroundDrawable(colorDrawable);
        } else {
            mViewColorable.setBackground(colorDrawable);
        }

        mLinearLayoutManager = mOrientationState.getLayoutManager(getContext());
        mRvCategories.setLayoutManager(mLinearLayoutManager);

        if (isInEditMode()) {
            setCategoriesAdapter(new SimpleCategoriesAdapter(MockedItemsFactory.getCategoryItems(getContext())));
        }

        int menuId = typedArray.getResourceId(R.styleable.LoopBarView_enls_menu, -1);
        if (menuId != -1) {
            setCategoriesAdapterFromMenu(menuId);
        }
        typedArray.recycle();
    }

    /**
     * Gets current value of selector gravity
     *
     * @return int constant representing current gravity for selector.
     * Will be one of {@link GravityAttr}
     */
    @GravityAttr
    public final int getGravity() {
        return mSelectionGravity;
    }

    /**
     * Sets new gravity for selector
     *
     * @param selectionGravity int value of gravity. Must be one of {@link GravityAttr}
     */
    public final void setGravity(@GravityAttr int selectionGravity) {
        mOrientationState.setSelectionGravity(selectionGravity);
        //note that mFlContainerSelected should be in FrameLayout
        FrameLayout.LayoutParams params = (LayoutParams) mFlContainerSelected.getLayoutParams();
        params.gravity = mOrientationState.getSelectionGravity();
        mOrientationState.setSelectionMargin(mSelectionMargin, params);
        mFlContainerSelected.setLayoutParams(params);
        mSelectionGravity = selectionGravity;
        invalidate();
        if (mOuterAdapter != null) {
            mOuterAdapter.setSelectedGravity(selectionGravity);
        }
    }

    /**
     * Sets scroll mode to infinite or finite
     *
     * @param isInfinite value presents is scroll mode need to be infinite
     * @deprecated use {@link #setScrollMode(int)} instead
     */
    @Deprecated
    public void setIsInfinite(boolean isInfinite) {
        setScrollMode(isInfinite ? SCROLL_MODE_INFINITE : SCROLL_MODE_FINITE);
    }

    private void changeScrolling(boolean isInfinite) {
        if (mInfinite != isInfinite) {
            mInfinite = isInfinite;
            if (mOuterAdapter != null) {
                mOuterAdapter.setIsIndeterminate(isInfinite);
            }
            checkAndScroll();
        }
    }

    /**
     * Returns constant representing current scroll mode
     *
     * @return one of {@link ScrollAttr}
     */
    @ScrollAttr
    public final int getScrollMode() {
        return mScrollMode;
    }

    /**
     * Sets new Scroll mode for LoopBar
     *
     * @param scrollMode must be one of {@link ScrollAttr}
     */
    public final void setScrollMode(@ScrollAttr int scrollMode) {
        if (scrollMode != mScrollMode) {
            mScrollMode = scrollMode;
            validateScrollMode();
        }
    }

    /**
     * Returns boolean value representing if LoopBar is in infinite mode or not
     *
     * @return true if LoopBar is in infinite mode or false if is in finite
     */
    public boolean isInfinite() {
        return mInfinite;
    }

    private void validateScrollMode() {
        if (mScrollMode == SCROLL_MODE_AUTO) {
            if (mOrientationState != null
                    && mRvCategories != null
                    && mOuterAdapter != null) {
                boolean isFitOnScreen = mOrientationState.isItemsFitOnScreen(mRvCategories,
                        mOuterAdapter.getWrappedItems().size());
                changeScrolling(!isFitOnScreen);
            }
        } else if (mScrollMode == SCROLL_MODE_INFINITE) {
            changeScrolling(true);
        } else if (mScrollMode == SCROLL_MODE_FINITE) {
            changeScrolling(false);
        }
    }

    /**
     * Initiate LoopBar with RecyclerView adapter
     *
     * @param inputAdapter Instance of {@link RecyclerView.Adapter}
     */
    public void setCategoriesAdapter(@NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter) {
        mInputAdapter = inputAdapter;
        mOuterAdapter = new ChangeScrollModeAdapter(inputAdapter);
        boolean hasItems = inputAdapter.getItemCount() > 0;
        if (hasItems) {
            IOperationItem firstItem = mOuterAdapter.getItem(0);
            firstItem.setVisible(false);
        }
        validateScrollMode();
        mOuterAdapter.setIsIndeterminate(mInfinite);
        mOuterAdapter.setListener(this);
        mOuterAdapter.setOrientation(mOrientationState.getOrientation());
        mOuterAdapter.setSelectedGravity(mSelectionGravity);
        mRvCategories.setAdapter(mOuterAdapter);

        mSelectorHolder = (ChangeScrollModeAdapter.ChangeScrollModeHolder) mOuterAdapter
                .createViewHolder(mRvCategories, ChangeScrollModeAdapter.VIEW_TYPE_CHANGE_SCROLL_MODE);
        // set first item to selectionView
        if (hasItems) {
            mSelectorHolder.bindItemWildcardHelper(inputAdapter, 0);
        }
        mSelectorHolder.itemView.setBackgroundColor(mColorCodeSelectionView);

        mFlContainerSelected.addView(mSelectorHolder.itemView);

        mOrientationState.initSelectionContainer(mFlContainerSelected);

        FrameLayout.LayoutParams layoutParams = (LayoutParams) mSelectorHolder.itemView.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
    }

    /**
     * Initiate LoopBar with menu
     *
     * @param menuRes id for inflating {@link Menu}
     */
    public void setCategoriesAdapterFromMenu(@MenuRes int menuRes) {
        Menu menu = new MenuBuilder(getContext());
        new MenuInflater(getContext()).inflate(menuRes, menu);
        setCategoriesAdapterFromMenu(menu);
    }

    /**
     * Initiate LoopBar with menu
     *
     * @param menu Instance of {@link Menu}
     */
    public void setCategoriesAdapterFromMenu(@NonNull Menu menu) {
        setCategoriesAdapter(new SimpleCategoriesMenuAdapter(menu));
    }

    /**
     * You can setup {@code {@link LoopBarView#mOuterAdapter }} through {@link ViewPager} adapter.
     * Your {@link ViewPager} adapter must implement {@link ILoopBarPagerAdapter} otherwise - the icons will not be shown
     *
     * @param viewPager - viewPager, which must have {@link ILoopBarPagerAdapter}
     */
    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        List<ICategoryItem> categoryItems = new ArrayList<>(pagerAdapter.getCount());
        ILoopBarPagerAdapter loopBarPagerAdapter =
                pagerAdapter instanceof ILoopBarPagerAdapter
                        ? (ILoopBarPagerAdapter) pagerAdapter : null;
        for (int i = 0, size = pagerAdapter.getCount(); i < size; i++) {
            categoryItems.add(new CategoryItem(
                    loopBarPagerAdapter != null ? loopBarPagerAdapter.getPageDrawable(i) : null,
                    String.valueOf(pagerAdapter.getPageTitle(i))
            ));
        }
        setCategoriesAdapter(new SimpleCategoriesAdapter(categoryItems));
    }

    /**
     * Add item click listener to this view
     *
     * @param itemClickListener Instance of {@link OnItemClickListener}
     * @return always true.
     */
    @SuppressWarnings("unused")
    public boolean addOnItemClickListener(OnItemClickListener itemClickListener) {
        return mClickListeners.add(itemClickListener);
    }

    /**
     * Remove item click listener from this view
     *
     * @param itemClickListener Instance of {@link OnItemClickListener}
     * @return true if this {@code List} was modified by this operation, false
     * otherwise.
     */
    @SuppressWarnings("unused")
    public boolean removeOnItemClickListener(OnItemClickListener itemClickListener) {
        return mClickListeners.remove(itemClickListener);
    }

    private void notifyItemClickListeners(int normalizedPosition) {
        for (OnItemClickListener itemClickListener : mClickListeners) {
            itemClickListener.onItemClicked(normalizedPosition);
        }
    }

    /**
     * Returns RecyclerView wrapped inside of view for control animations
     * Don't use it for changing adapter inside.
     * Use {@link #setCategoriesAdapter(RecyclerView.Adapter)} instead
     *
     * @return instance of {@link RecyclerView}
     * @deprecated use {@link #setItemAnimator(RecyclerView.ItemAnimator)},
     * {@link #isAnimating()},
     * {@link #addItemDecoration(RecyclerView.ItemDecoration)},
     * {@link #addItemDecoration(RecyclerView.ItemDecoration, int)},
     * {@link #removeItemDecoration(RecyclerView.ItemDecoration)},
     * {@link #invalidateItemDecorations()},
     * {@link #addOnScrollListener(RecyclerView.OnScrollListener)},
     * {@link #removeOnScrollListener(RecyclerView.OnScrollListener)}
     * {@link #clearOnScrollListeners()} instead
     */
    @Deprecated
    public RecyclerView getWrappedRecyclerView() {
        return mRvCategories;
    }

    private RecyclerView getRvCategories() {
        return mRvCategories;
    }

    /**
     * Sets the {@link RecyclerView.ItemAnimator} that will handle animations involving changes
     * to the items in wrapped RecyclerView. By default, RecyclerView instantiates and
     * uses an instance of {@link DefaultItemAnimator}. Whether item animations are
     * enabled for the RecyclerView depends on the ItemAnimator and whether
     * the LayoutManager {@link RecyclerView.LayoutManager#supportsPredictiveItemAnimations()
     * supports item animations}.
     *
     * @param animator The ItemAnimator being set. If null, no animations will occur
     *                 when changes occur to the items in this RecyclerView.
     */
    @SuppressWarnings("unused")
    public final void setItemAnimator(RecyclerView.ItemAnimator animator) {
        getRvCategories().setItemAnimator(animator);
    }

    /**
     * Returns true if wrapped RecyclerView is currently running some animations.
     * <p>
     * If you want to be notified when animations are finished, use
     * {@link RecyclerView.ItemAnimator#isRunning(RecyclerView.ItemAnimator.ItemAnimatorFinishedListener)}.
     *
     * @return True if there are some item animations currently running or waiting to be started.
     */
    @SuppressWarnings("unused")
    public final boolean isAnimating() {
        return getRvCategories().isAnimating();
    }

    /**
     * Add an {@link RecyclerView.ItemDecoration} to wrapped RecyclerView. Item decorations can
     * affect both measurement and drawing of individual item views.
     * <p>
     * <p>Item decorations are ordered. Decorations placed earlier in the list will
     * be run/queried/drawn first for their effects on item views. Padding added to views
     * will be nested; a padding added by an earlier decoration will mean further
     * item decorations in the list will be asked to draw/pad within the previous decoration's
     * given area.</p>
     *
     * @param decor Decoration to add
     */
    @SuppressWarnings("unused")
    public final void addItemDecoration(RecyclerView.ItemDecoration decor) {
        getRvCategories().addItemDecoration(decor);
    }

    /**
     * Add an {@link RecyclerView.ItemDecoration} to wrapped RecyclerView. Item decorations can
     * affect both measurement and drawing of individual item views.
     * <p>
     * <p>Item decorations are ordered. Decorations placed earlier in the list will
     * be run/queried/drawn first for their effects on item views. Padding added to views
     * will be nested; a padding added by an earlier decoration will mean further
     * item decorations in the list will be asked to draw/pad within the previous decoration's
     * given area.</p>
     *
     * @param decor Decoration to add
     * @param index Position in the decoration chain to insert this decoration at. If this value
     *              is negative the decoration will be added at the end.
     */
    @SuppressWarnings("unused")
    public final void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        getRvCategories().addItemDecoration(decor, index);
    }

    /**
     * Remove an {@link RecyclerView.ItemDecoration} from wrapped RecyclerView.
     * <p>
     * <p>The given decoration will no longer impact the measurement and drawing of
     * item views.</p>
     *
     * @param decor Decoration to remove
     * @see #addItemDecoration(RecyclerView.ItemDecoration)
     */
    @SuppressWarnings("unused")
    public final void removeItemDecoration(RecyclerView.ItemDecoration decor) {
        getRvCategories().removeItemDecoration(decor);
    }

    /**
     * Invalidates all ItemDecorations in wrapped RecyclerView. If RecyclerView has item decorations, calling this method
     * will trigger a {@link #requestLayout()} call.
     */
    @SuppressWarnings("unused")
    public final void invalidateItemDecorations() {
        getRvCategories().invalidateItemDecorations();
    }

    /**
     * Add a listener to wrapped RecyclerView that will be notified of any changes in scroll state or position.
     * <p>
     * <p>Components that add a listener should take care to remove it when finished.
     * Other components that take ownership of a view may call {@link #clearOnScrollListeners()}
     * to remove all attached listeners.</p>
     *
     * @param listener listener to set or null to clear
     */
    @SuppressWarnings("unused")
    public final void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        getRvCategories().addOnScrollListener(listener);
    }

    /**
     * Remove a listener from wrapped RecyclerView that was notified of any changes in scroll state or position.
     *
     * @param listener listener to set or null to clear
     */
    @SuppressWarnings("unused")
    public final void removeOnScrollListener(RecyclerView.OnScrollListener listener) {
        getRvCategories().removeOnScrollListener(listener);
    }

    /**
     * Remove all secondary listener from wrapped RecyclerView that were notified of any changes in scroll state or position.
     */
    @SuppressWarnings("unused")
    public final void clearOnScrollListeners() {
        getRvCategories().clearOnScrollListeners();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mOverlayPlaceholder = ((ViewGroup) getParent()).findViewById(mPlaceHolderId);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        int adapterSize = ss.mAdapterSize;
        if (adapterSize > 0) {
            setCurrentItem(ss.mCurrentItemPosition);
        }
        setGravity(ss.mSelectionGravity);
        mScrollMode = ss.mScrollMode;
        changeScrolling(ss.mIsInfinite);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!mSkipNextOnLayout) {

            if (mOverlaySize > 0 && mOverlayPlaceholder == null) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "You have to add placeholder and set it id with #enls_placeHolderId parameter to use mOverlaySize");
                }
            }

            mOrientationState.initPlaceHolderAndOverlay(mOverlayPlaceholder, mRvCategories,
                    mFlContainerSelected, mOverlaySize);

            if (mRvCategories.getChildCount() > 0 && !mIndeterminateInitialized) {
                mIndeterminateInitialized = true;
                //scroll to middle of indeterminate recycler view on initialization and if user somehow scrolled to start or end
                mRvCategories.getViewTreeObserver()
                        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                boolean isFitOnScreen = mOrientationState
                                        .isItemsFitOnScreen(mRvCategories, mOuterAdapter.getWrappedItems().size());
                                if (mScrollMode == SCROLL_MODE_AUTO) {
                                    changeScrolling(!isFitOnScreen);
                                }
                                checkAndScroll();
                                updateCategoriesOffsetBySelector(!mInfinite);
                                mRvCategories.addOnScrollListener(mIndeterminateOnScrollListener);
                                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    mRvCategories.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                } else {
                                    mRvCategories.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }

                            }
                        });
            }
            mSkipNextOnLayout = true;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        int adapterSize = mInputAdapter != null ? mInputAdapter.getItemCount() : 0;
        return new SavedState(superState,
                mCurrentItemPosition, mSelectionGravity, mInfinite, mScrollMode,
                adapterSize);
    }

    private void startSelectedViewOutAnimation(final int position) {
        Animator animator = mSelectionOutAnimator;
        animator.setTarget(mSelectorHolder.itemView);
        animator.start();
        animator.addListener(new AbstractAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //replace selected view
                mSelectorHolder.bindItemWildcardHelper(mInputAdapter, position);
                startSelectedViewInAnimation();
            }
        });
    }

    private void startSelectedViewInAnimation() {
        Animator animator = mSelectionInAnimator;
        animator.setTarget(mSelectorHolder.itemView);
        animator.addListener(new AbstractAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                updateCategoriesOffsetBySelector();
            }
        });
        animator.start();
    }

    private void updateCategoriesOffsetBySelector() {
        if (mSelectorHolder == null) {
            return;
        }
        int size;
        if (mOrientationState != null) {
            size = mOrientationState.getSize(mSelectorHolder.itemView) + mSelectionMargin;
        } else {
            size = 0;
        }
        updateCategoriesOffset(size);
    }

    private void updateCategoriesOffsetBySelector(boolean withRecyclerViewNotification) {
        if (mSelectorHolder == null) {
            return;
        }
        int size;
        if (mOrientationState != null) {
            size = mOrientationState.getSize(mSelectorHolder.itemView)
                    + mOrientationState.getHeaderMargins(getContext())
                    + mSelectionMargin;
        } else {
            size = 0;
        }
        updateCategoriesOffset(size, withRecyclerViewNotification);
    }

    /**
     * Set selected item in endless view. OnItemSelected listeners won't be invoked
     *
     * @param currentItemPosition selected position
     */
    @SuppressWarnings("unused")
    public void setCurrentItem(int currentItemPosition) {
        selectItem(currentItemPosition, false);
    }

    /**
     * Set selected item in endless view.
     * OnItemSelected listeners won't be invoked
     *
     * @param currentItemPosition selected position
     * @param isInvokeListeners   should view notify OnItemSelected listeners about this selection
     */
    @SuppressWarnings("unused")
    public void setCurrentItem(int currentItemPosition, boolean isInvokeListeners) {
        selectItem(currentItemPosition, isInvokeListeners);
    }

    /**
     * Select item by its' position
     *
     * @param position        int value of item position to select
     * @param invokeListeners boolean value for invoking listeners
     */
    @SuppressWarnings("unused")
    public void selectItem(int position, boolean invokeListeners) {
        IOperationItem item = mOuterAdapter.getItem(position);
        IOperationItem oldHidedItem = mOuterAdapter.getItem(mRealHidedPosition);

        int realPosition = mOuterAdapter.normalizePosition(position);
        //do nothing if position not changed
        if (realPosition == mCurrentItemPosition) {
            return;
        }
        int itemToShowAdapterPosition = position - realPosition + mRealHidedPosition;

        item.setVisible(false);

        startSelectedViewOutAnimation(position);

        mOuterAdapter.notifyRealItemChanged(position);
        mRealHidedPosition = realPosition;

        oldHidedItem.setVisible(true);
        mFlContainerSelected.requestLayout();
        mOuterAdapter.notifyRealItemChanged(itemToShowAdapterPosition);
        mCurrentItemPosition = realPosition;

        if (invokeListeners) {
            notifyItemClickListeners(realPosition);
        }

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "clicked on position =" + position);
        }

    }

    /**
     * Select item by its' position. Listeners will be invoked
     *
     * @param position int value of item position to select
     */
    @Override
    public void onItemClicked(int position) {
        selectItem(position, true);
    }

    //orientation state factory method
    public IOrientationState getOrientationStateFromParam(int orientation) {
        return orientation == Orientation.ORIENTATION_VERTICAL
                ? new OrientationStateVertical()
                : new OrientationStateHorizontal();
    }

    private void checkAndScroll() {
        if (isInfinite() && (mLinearLayoutManager.findFirstVisibleItemPosition() == 0
                || mLinearLayoutManager.findFirstVisibleItemPosition() == 1
                || mLinearLayoutManager.findLastVisibleItemPosition() == Integer.MAX_VALUE)) {
            mLinearLayoutManager.scrollToPositionWithOffset(getPositionForScroll(), getScrollOffset());
        }
    }

    private int getPositionForScroll() {
        if (mOuterAdapter == null || mOuterAdapter.getWrappedItems().isEmpty()) {
            return Integer.MAX_VALUE / 2;
        }
        int size = mOuterAdapter.getWrappedItems().size();
        int count = (Integer.MAX_VALUE / 2) / size;

        return count * size;
    }

    private int getScrollOffset() {
        int headerOffset;
        if (mSelectionGravity == SELECTION_GRAVITY_START
                && mOrientationState != null
                && mSelectorHolder != null) {
            headerOffset = mOrientationState.getSize(mSelectorHolder.itemView)
                    + mOrientationState.getHeaderMargins(getContext())
                    + mSelectionMargin;
        } else {
            headerOffset = 0;
        }

        return headerOffset;
    }

    private void updateCategoriesOffset(int size) {
        updateCategoriesOffset(size, true);
    }

    private void updateCategoriesOffset(int size, boolean withAdapterNotification) {
        if (mOuterAdapter != null) {
            mOuterAdapter.setHeaderSize(size);
            if (withAdapterNotification) {
                if (mSelectionGravity == SELECTION_GRAVITY_START) {
                    mOuterAdapter.notifyItemChanged(0);
                } else {
                    mOuterAdapter.notifyItemChanged(mOuterAdapter.getItemCount() - 1);
                }
            }
        }
    }

    /**
     * Interface with pre-defined limit of gravity constants for selector
     *
     * @see #SELECTION_GRAVITY_START
     * @see #SELECTION_GRAVITY_END
     */
    @IntDef({SELECTION_GRAVITY_START, SELECTION_GRAVITY_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityAttr {
    }

    /**
     * Interface with pre-defined limit of constants for scroll mode
     *
     * @see #SCROLL_MODE_AUTO
     * @see #SCROLL_MODE_INFINITE
     * @see #SCROLL_MODE_FINITE
     */
    @IntDef({SCROLL_MODE_AUTO, SCROLL_MODE_INFINITE, SCROLL_MODE_FINITE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollAttr {
    }

    /**
     * Encapsulate logic for LoopBar saving and restore state
     *
     * @see #onSaveInstanceState()
     * @see #onRestoreInstanceState(Parcelable)
     */
    public static class SavedState extends BaseSavedState implements Parcelable {

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private int mCurrentItemPosition;
        @GravityAttr
        private int mSelectionGravity;
        @ScrollAttr
        private int mScrollMode;
        private boolean mIsInfinite;
        private int mAdapterSize;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @SuppressWarnings("unused")
        private SavedState(Parcel parcel) {
            super(parcel);
            mCurrentItemPosition = parcel.readInt();
            @GravityAttr
            int gravity = parcel.readInt();
            mSelectionGravity = gravity;
            @ScrollAttr
            int scrollMode = parcel.readInt();
            mScrollMode = scrollMode;
            boolean[] booleanValues = new boolean[1];
            parcel.readBooleanArray(booleanValues);
            mIsInfinite = booleanValues[0];
            mAdapterSize = parcel.readInt();
        }

        SavedState(Parcelable superState, int currentItemPosition,
                   int selectionGravity, boolean isInfinite, int scrollMode, int adapterSize) {
            super(superState);
            mCurrentItemPosition = currentItemPosition;
            mSelectionGravity = selectionGravity;
            mIsInfinite = isInfinite;
            mScrollMode = scrollMode;
            mAdapterSize = adapterSize;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeInt(mCurrentItemPosition);
            parcel.writeInt(mSelectionGravity);
            parcel.writeInt(mScrollMode);
            parcel.writeBooleanArray(new boolean[]{mIsInfinite});
            parcel.writeInt(mAdapterSize);
        }
    }

    private static class IndeterminateOnScrollListener extends RecyclerView.OnScrollListener {

        private final WeakReference<LoopBarView> loopBarViewWeakReference;

        private IndeterminateOnScrollListener(LoopBarView loopBarView) {
            loopBarViewWeakReference = new WeakReference<>(loopBarView);

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LoopBarView loopBarView = loopBarViewWeakReference.get();
            if (loopBarView != null) {
                loopBarView.checkAndScroll();
            }

        }
    }
}
