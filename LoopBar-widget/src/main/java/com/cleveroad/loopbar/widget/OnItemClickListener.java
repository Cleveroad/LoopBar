package com.cleveroad.loopbar.widget;

/**
 * Simple onClick listener for items in LoopBar
 */
public interface OnItemClickListener {

    /**
     * Must be called when user clicks on item in LoopBar
     *
     * @param position int value of item position in LoopBar
     */
    void onItemClicked(int position);
}
