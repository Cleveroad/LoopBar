package com.cleveroad.loopbar.widget;

/**
 * Interface for providing changing of view size
 */
interface IChangeSizeCallback {

    /**
     * Returns new size for view in pixels.
     * You can want to ignore it if it is 0
     *
     * @return int value of new size in pixels
     */
    int getHeaderSize();

    /**
     * Returns current orientation state
     *
     * @return One of {@link Orientation}
     * @see Orientation#ORIENTATION_HORIZONTAL_BOTTOM
     * @see Orientation#ORIENTATION_HORIZONTAL_TOP
     * @see Orientation#ORIENTATION_VERTICAL_LEFT
     * @see Orientation#ORIENTATION_VERTICAL_RIGHT
     */
    @Orientation
    int getOrientation();
}
