package tamhoang.ldpro4.ui.custom.switch

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import tamhoang.ldpro4.R

/**
 * This Switch has only 2 color (blue+gray) of trackTint, thumbTint always white
 * Created by VietBac
 * **/

class SwitchCustomColor : SwitchCompat {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    init {
        /** get Color to set without alpha **/
        val primaryColor = ContextCompat.getColor(context, R.color.primaryColor)
        val white = ContextCompat.getColor(context, R.color.white)
        val darkGray = ContextCompat.getColor(context, R.color.dark_gray)
        // Sets the tints for the thumb in different states
        DrawableCompat.setTintList(thumbDrawable,
                ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_checked),
                                intArrayOf(-android.R.attr.state_checked)),
                        intArrayOf(white, white)
                ))
        /** Sets the tints for the track in different states **/
        DrawableCompat.setTintList(trackDrawable,
                ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked),
                                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
                                intArrayOf(-android.R.attr.state_enabled, android.R.attr.state_checked),
                                intArrayOf(-android.R.attr.state_enabled, -android.R.attr.state_checked)),
                        intArrayOf(primaryColor,
                                darkGray,
                                primaryColor,
                                darkGray,
                                darkGray)
                )
        )
    }
}