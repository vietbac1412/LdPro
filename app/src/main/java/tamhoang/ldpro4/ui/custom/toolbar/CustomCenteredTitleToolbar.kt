package tamhoang.ldpro4.ui.custom.toolbar

import android.content.Context
import android.graphics.Point
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import timber.log.Timber
import tamhoang.ldpro4pos365.injection.ApplicationContext
import javax.inject.Inject

/**
 * This toolbar allows menu items to be inflated as standard, without affecting the centeredness of the title
 */

class CustomCenteredTitleToolbar : Toolbar {

    var mContext: Context? = null

    private var _layoutParent : LinearLayout? = null
    private var _titleTextView: TextView? = null
    private var _subTitleTextView : TextView? = null
    private var _screenWidth = 0
    private var _centerTitle = true

    constructor(context: Context) : super(context) {
        mContext = context
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        init()
    }

    private fun init() {
        _screenWidth = screenSize().x
        _layoutParent = LinearLayout(context)
        _layoutParent!!.orientation = LinearLayout.VERTICAL
        _titleTextView = TextView(context)
        _titleTextView!!.textSize = 16f
        _titleTextView!!.typeface = Typeface.createFromAsset(mContext?.assets, "fonts/Montserrat-Bold.ttf")

        _subTitleTextView = TextView(context)
        _subTitleTextView!!.visibility = View.GONE
        _subTitleTextView!!.typeface = Typeface.createFromAsset(mContext?.assets, "fonts/Montserrat-Medium.ttf")
        _subTitleTextView!!.textSize = 14f

        addView(_layoutParent)
        _layoutParent!!.addView(_titleTextView)
        _layoutParent!!.addView(_subTitleTextView)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (_centerTitle) {
            val location = IntArray(2)
            _layoutParent!!.getLocationOnScreen(location)
            _layoutParent!!.translationX = _layoutParent!!.translationX + (-location[0] + _screenWidth / 2 - _layoutParent!!.width / 2)
            _titleTextView!!.gravity = Gravity.CENTER_HORIZONTAL
            _subTitleTextView!!.gravity = Gravity.CENTER_HORIZONTAL
        } else {
            val location = IntArray(2)
            _layoutParent!!.getLocationOnScreen(location)
            _layoutParent!!.translationX = 0F
            _titleTextView!!.gravity = Gravity.START
            _subTitleTextView!!.gravity = Gravity.START
        }
    }

    override fun setTitle(title: CharSequence?) {
        _titleTextView!!.text = title
        requestLayout()
    }

    override fun setTitle(titleRes: Int) {
        _titleTextView!!.setText(titleRes)
        requestLayout()
    }

    override fun setTitleTextColor(color: Int) {
        _titleTextView!!.setTextColor(color)
        requestLayout()
    }

    override fun setSubtitle(subtitle: CharSequence?) {
        _subTitleTextView!!.text = subtitle
        requestLayout()
    }

    override fun setSubtitle(resId: Int) {
        _subTitleTextView!!.setText(resId)
        requestLayout()
    }

    override fun setSubtitleTextColor(color: Int) {
        _subTitleTextView!!.setTextColor(color)
        requestLayout()
    }

    fun showSubTitle(isShow : Boolean){
        if (isShow){
            _subTitleTextView!!.visibility = View.VISIBLE
        }
        else{
            _subTitleTextView!!.visibility = View.GONE
        }
        requestLayout()
    }

    fun setTitleCentered(centered: Boolean) {
        _centerTitle = centered
        requestLayout()
    }


    private fun screenSize() : Point{
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val screenSize = Point()
            display.getSize(screenSize)
            return screenSize
        }
}