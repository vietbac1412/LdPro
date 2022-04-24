package tamhoang.ldpro4.anim

import android.view.animation.Interpolator

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 27/07/2018.
 */
class BounceInterpolator(private val amplitude: Double = 1.0,
                         private val frequency: Double = 10.0) : Interpolator {

    override fun getInterpolation(time: Float): Float {
        return (-1 * Math.pow(Math.E, - time / amplitude) * Math.cos(frequency * time) + 1).toFloat()
    }

}