package tamhoang.ldpro4.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView

class RotationImageView : androidx.appcompat.widget.AppCompatImageView {

    var angleRotation = 0F
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onDraw(canvas: Canvas?) {
        canvas?.save();
        canvas?.rotate(angleRotation, (width / 2).toFloat(), (height/2).toFloat())
        super.onDraw(canvas)
        canvas?.restore()
    }

}