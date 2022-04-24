package tamhoang.ldpro4pos365.util.extension

import android.content.res.Resources

/**
 * @author  : Hanet Electronics
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : muoick@hanet.com
 * @Website : http://hanet.com/
 * @Project : Music
 * Created by chukimmuoi on 10/02/2018.
 */

// https://stackoverflow.com/questions/4605527/converting-pixels-to-dp#12147550

/**
 * Converts a pixel value to a density independent pixels (DPs).
 *
 * @param resources A reference to the [Resources] in the current context.
 * @return The value of {@code pixels} in DPs.
 */
fun Float.convertPixelToDp(resources: Resources) : Float {
    return this / resources.displayMetrics.density
}

/**
 * Converts a density independent pixels (DPs) value to a pixel.
 *
 * @param resources A reference to the [Resources] in the current context.
 * @return The value of {@code Dps} in pixels.
 */
fun Float.convertDpToPixel(resources: Resources) : Float {
    return this * resources.displayMetrics.density
}

/**
 * Converts a pixel value to a density independent pixels (DPs).
 *
 * @param resources A reference to the [Resources] in the current context.
 * @return The value of {@code pixels} in DPs.
 */
fun Int.convertPixelToDp(resources: Resources) : Int {
    return Math.round(this / resources.displayMetrics.density)
}

/**
 * Converts a density independent pixels (DPs) value to a pixel.
 *
 * @param resources A reference to the [Resources] in the current context.
 * @return The value of {@code Dps} in pixels.
 */
fun Int.convertDpToPixel(resources: Resources) : Int{
    return Math.round(this * resources.displayMetrics.density)
}

fun Int.getCountAndSpaceInGrid(resources: Resources, percent: Float) : Pair<Int, Int> {
    var output = Pair(0, 0)
    try {
        val widthFullScreen = resources.displayMetrics.widthPixels
        val widthUseCalculator = widthFullScreen * percent
        val spanCount = (widthUseCalculator / this).toInt()
        val space = ((widthUseCalculator - spanCount * this) / (spanCount + 1) / 2).toInt()
        output = Pair(spanCount, space)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return output
}

fun Int.getCountAndSpaceInGrid(resources: Resources, percent: Float, minSpace: Int = 0) : Pair<Int, Int> {
    var output = Pair(0, 0)
    try {
        val widthFullScreen = resources.displayMetrics.widthPixels
        val widthUseCalculator = widthFullScreen * percent
        var spanCount = (widthUseCalculator / this).toInt()
        var space = ((widthUseCalculator - spanCount * this) / spanCount / 2).toInt()
        if (space < minSpace) {
            space = ((widthUseCalculator - spanCount * this) / (spanCount - 1) / 2).toInt() + this / (spanCount - 1) / 2
            spanCount -= 1
        }

        output = Pair(spanCount, space)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return output
}

fun Int.getCountAndSpaceInFlexStart(resources: Resources, percent: Float, margin: Int, minSpace: Int = 0): Pair<Int, Int> {
    var output = Pair(0, 0)
    try {
        val widthFullScreen = resources.displayMetrics.widthPixels
        val widthUseCalculator = widthFullScreen * percent - margin
        val spanCount = (widthUseCalculator / this).toInt()
        var space = ((widthUseCalculator - spanCount * this) / spanCount / 2).toInt()
        if (space < minSpace)
            space = ((widthUseCalculator - spanCount * this) / (spanCount - 1) / 2).toInt() + this / (spanCount - 1) / 2

        output = Pair(spanCount, space)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return output
}

fun Double.getPixelsFollowPercent(resources: Resources) : Int? {
    var output : Int? = null
    try {
        val widthFullScreen = resources.displayMetrics.widthPixels
        val widthUseCalculator = widthFullScreen * this
        output = widthUseCalculator.toInt()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return output
}