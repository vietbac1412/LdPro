package tamhoang.ldpro4.ui.custom.radioGroup

import android.content.Context
import android.content.res.TypedArray

import android.util.AttributeSet

import android.view.View
import android.view.ViewGroup

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.CompoundButton

import android.widget.RadioButton
import android.widget.RadioGroup

import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import tamhoang.ldpro4.R

class FlexBoxRadioGroup : FlexboxLayout {


    // holds the checked id; the selection is empty by default
    /**
     *
     * Returns the identifier of the selected radio button in this group.
     * Upon empty selection, the returned value is -1.
     *
     * @return the unique id of the selected radio button in this group
     * @attr ref android.R.styleable#RadioGroup_checkedButton
     * @see .check
     * @see .clearCheck
     */
    var checkedRadioButtonId = -1
        private set
    // tracks children radio buttons checked state
    private var mChildOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
    // when true, mOnCheckedChangeListener discards events
    private var mProtectFromCheckedChange = false
    private var mOnCheckedChangeListener: FlexBoxRadioGroup.OnCheckedChangeListener? = null
    private var mPassThroughListener: PassThroughHierarchyChangeListener? = null

    /**
     * {@inheritDoc}
     */
    constructor(context: Context) : super(context) {
        flexDirection = FlexDirection.ROW
        init()
    }

    /**
     * {@inheritDoc}
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        // retrieve selected radio button as requested by the user in the
        // XML layout file
        val attributes = context.obtainStyledAttributes(
                attrs, R.styleable.FlexBoxRadioGroup)

        val value = attributes.getResourceId(R.styleable.FlexBoxRadioGroup_checkedRadioButton, View.NO_ID)
        if (value != View.NO_ID) {
            checkedRadioButtonId = value
        }

        attributes.recycle()
        init()
    }

    private fun init() {
        mChildOnCheckedChangeListener = CheckedStateTracker()
        mPassThroughListener = PassThroughHierarchyChangeListener()
        super.setOnHierarchyChangeListener(mPassThroughListener)
    }

    /**
     * {@inheritDoc}
     */
    override fun setOnHierarchyChangeListener(listener: ViewGroup.OnHierarchyChangeListener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener!!.mOnHierarchyChangeListener = listener
    }

    /**
     * {@inheritDoc}
     */
    override fun onFinishInflate() {
        super.onFinishInflate()

        // checks the appropriate radio button as requested in the XML file
        if (checkedRadioButtonId != -1) {
            mProtectFromCheckedChange = true
            setCheckedStateForView(checkedRadioButtonId, true)
            mProtectFromCheckedChange = false
            setCheckedId(checkedRadioButtonId)
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is RadioButton) {
            if (child.isChecked) {
                mProtectFromCheckedChange = true
                if (checkedRadioButtonId != -1) {
                    setCheckedStateForView(checkedRadioButtonId, false)
                }
                mProtectFromCheckedChange = false
                setCheckedId(child.id)
            }
        }

        super.addView(child, index, params)
    }

    /**
     *
     * Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking [.clearCheck].
     *
     * @param id the unique id of the radio button to select in this group
     * @see .getCheckedRadioButtonId
     * @see .clearCheck
     */
    fun check(id: Int) {
        // don't even bother
        if (id != -1 && id == checkedRadioButtonId) {
            return
        }

        if (checkedRadioButtonId != -1) {
            setCheckedStateForView(checkedRadioButtonId, false)
        }

        if (id != -1) {
            setCheckedStateForView(id, true)
        }

        setCheckedId(id)
    }

    private fun setCheckedId(id: Int) {
        checkedRadioButtonId = id
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener!!.onCheckedChanged(this, checkedRadioButtonId)
        }
    }

    private fun setCheckedStateForView(viewId: Int, checked: Boolean) {
        val checkedView = findViewById<View>(viewId)
        if (checkedView != null && checkedView is RadioButton) {
            checkedView.isChecked = checked
        }
    }

    /**
     *
     * Clears the selection. When the selection is cleared, no radio button
     * in this group is selected and [.getCheckedRadioButtonId] returns
     * null.
     *
     * @see .check
     * @see .getCheckedRadioButtonId
     */
    fun clearCheck() {
        check(-1)
    }

    /**
     *
     * Register a callback to be invoked when the checked radio button
     * changes in this group.
     *
     * @param listener the callback to call on checked state change
     */
    fun setOnCheckedChangeListener(listener: FlexBoxRadioGroup.OnCheckedChangeListener) {
        mOnCheckedChangeListener = listener
    }

    /**
     * {@inheritDoc}
     */
    //    @Override
    //    public FlexBoxRadioGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
    //        return new FlexBoxRadioGroup.LayoutParams(getContext(), attrs);
    //    }

    /**
     * {@inheritDoc}
     */
    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is RadioGroup.LayoutParams
    }

    override fun generateDefaultLayoutParams(): FlexboxLayout.LayoutParams {
        return FlexBoxRadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = RadioGroup::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = RadioGroup::class.java.name
    }

    /**
     *
     * This set of layout parameters defaults the width and the height of
     * the children to [.WRAP_CONTENT] when they are not specified in the
     * XML file. Otherwise, this class uses the value read from the XML file.
     */
    class LayoutParams : FlexboxLayout.LayoutParams {
        /**
         * {@inheritDoc}
         */
        constructor(c: Context, attrs: AttributeSet) : super(c, attrs)

        /**
         * {@inheritDoc}
         */
        constructor(w: Int, h: Int) : super(w, h)

        //        /**
        //         * {@inheritDoc}
        //         */
        //        public LayoutParams(int w, int h, float initWeight) {
        //            super(w, h, initWeight);
        //        }

        /**
         * {@inheritDoc}
         */
        constructor(p: ViewGroup.LayoutParams) : super(p)

        /**
         * {@inheritDoc}
         */
        constructor(source: ViewGroup.MarginLayoutParams) : super(source)

        /**
         *
         * Fixes the child's width to
         * [android.view.ViewGroup.LayoutParams.WRAP_CONTENT] and the child's
         * height to  [android.view.ViewGroup.LayoutParams.WRAP_CONTENT]
         * when not specified in the XML file.
         *
         * @param a          the styled attributes set
         * @param widthAttr  the width attribute to fetch
         * @param heightAttr the height attribute to fetch
         */
        override fun setBaseAttributes(a: TypedArray,
                                       widthAttr: Int, heightAttr: Int) {

            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width")
            } else {
                width = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height")
            } else {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    /**
     *
     * Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.
     */
    interface OnCheckedChangeListener {
        /**
         *
         * Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.
         *
         * @param group     the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        fun onCheckedChanged(group: FlexBoxRadioGroup, checkedId: Int)
    }

    private inner class CheckedStateTracker : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return
            }

            mProtectFromCheckedChange = true
            if (checkedRadioButtonId != -1) {
                setCheckedStateForView(checkedRadioButtonId, false)
            }
            mProtectFromCheckedChange = false

            val id = buttonView.id
            setCheckedId(id)
        }
    }

    /**
     *
     * A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.
     */
    private inner class PassThroughHierarchyChangeListener : ViewGroup.OnHierarchyChangeListener {
        var mOnHierarchyChangeListener: ViewGroup.OnHierarchyChangeListener? = null

        /**
         * {@inheritDoc}
         */
        override fun onChildViewAdded(parent: View, child: View) {
            if (parent === this@FlexBoxRadioGroup && child is RadioButton) {
                var id = child.getId()
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = child.hashCode()
                    child.setId(id)
                }
                child.setOnCheckedChangeListener(
                        mChildOnCheckedChangeListener)
            }

            mOnHierarchyChangeListener?.onChildViewAdded(parent, child)
        }

        /**
         * {@inheritDoc}
         */
        override fun onChildViewRemoved(parent: View, child: View) {
            if (parent === this@FlexBoxRadioGroup && child is RadioButton) {
                child.setOnCheckedChangeListener(null)
            }

            mOnHierarchyChangeListener?.onChildViewRemoved(parent, child)
        }
    }
}
