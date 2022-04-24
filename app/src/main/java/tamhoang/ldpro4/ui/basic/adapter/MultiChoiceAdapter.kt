package tamhoang.ldpro4.ui.partner.adapter

import android.app.Activity
import tamhoang.ldpro4.ui.basic.adapter.delegates.MultiChoiceDelegate
import tamhoang.ldpro4.ui.custom.recycle.BaseRecycleAdapter
import javax.inject.Inject

class MultiChoiceAdapter
@Inject constructor(val activity : Activity) : BaseRecycleAdapter(){
    fun setAdapter(action : (Int, Boolean) -> Unit) : MultiChoiceAdapter {
        delegateManager.addDelegate(MultiChoiceDelegate(activity,action))
        return this
    }
}