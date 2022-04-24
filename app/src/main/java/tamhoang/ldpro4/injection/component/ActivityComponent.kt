package tamhoang.ldpro4pos365.injection.component

import dagger.Subcomponent
import tamhoang.ldpro4.ui.main.MainActivity

import tamhoang.ldpro4pos365.injection.PerActivity
import tamhoang.ldpro4pos365.injection.modul.ActivityModule

@PerActivity
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun inject(activity: MainActivity) // Main
//    fun inject(fragment: OrderFragment)

}