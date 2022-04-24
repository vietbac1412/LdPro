package tamhoang.ldpro4.ui.custom.customkeyboard.components.expandableView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import tamhoang.ldpro4.ui.custom.customkeyboard.components.ResizableRelativeLayout

abstract class ExpandableView(
        context: Context, attr: AttributeSet
) :
        ResizableRelativeLayout(context, attr) {

    var fieldInFocus: EditText? = null
    private var state: ExpandableState? = null
    private val stateListeners = ArrayList<ExpandableStateListener>()

    val isExpanded: Boolean
        get() = state === ExpandableState.EXPANDED

    init {
        state = ExpandableState.EXPANDED // view is expanded when initially created
        visibility = View.INVISIBLE
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        translateLayout() // collapse view after initial inflation
    }

    fun registerListener(listener: ExpandableStateListener) {
        stateListeners.add(listener)
    }

    fun translateLayout() {
        // Ignore calls that occur during animation (prevents issues from wood-pecker'ing)
        if (state !== ExpandableState.EXPANDING && state !== ExpandableState.COLLAPSING) {
            val pixels = 500.toDp
            val millis : Long = pixels.toLong() / 5 // translates layout 5px per millisecond
            val deltaY: Float
            when (state) {
                ExpandableState.EXPANDED -> {
                    updateState(ExpandableState.COLLAPSING)
                    deltaY = pixels.toFloat() // pushes layout down 500 device pixels
                    animate().translationY(deltaY).setDuration(millis).withEndAction {
                        updateState(ExpandableState.COLLAPSED)
                        visibility = View.INVISIBLE
                    }.start()
                }
                ExpandableState.COLLAPSED -> {
                    updateState(ExpandableState.EXPANDING)
                    visibility = View.VISIBLE
                    deltaY = 0.0f // pulls layout back to its original position=
                    animate().translationY(deltaY).setDuration(millis).withEndAction {
                        updateState(ExpandableState.EXPANDED)
                    }.start()
                }
                else -> return
            }
        }
    }

    fun forceCollapse() {
        if (state == ExpandableState.COLLAPSED) return
        val pixels = 500.toDp
        val millis : Long = pixels.toLong() / 5 // translates layout 5px per millisecond
        val deltaY: Float
        updateState(ExpandableState.COLLAPSING)
        deltaY = pixels.toFloat() // pushes layout down 500 device pixels
        animate().translationY(deltaY).setDuration(millis).withEndAction {
            updateState(ExpandableState.COLLAPSED)
            visibility = View.INVISIBLE
        }.start()
    }

    private fun updateState(nextState: ExpandableState) {
        state = nextState
        for (listener in stateListeners) {
            listener.onStateChange(nextState)
        }
        if (nextState == ExpandableState.COLLAPSED) fieldInFocus?.clearFocus()
    }

    abstract override fun configureSelf()
}