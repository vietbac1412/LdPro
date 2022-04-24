package tamhoang.ldpro4.ui.custom.text

import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.tankery.lib.circularseekbar.CircularSeekBar

object RxViewObservable {
    fun fromSeekBar(seekBar: AppCompatSeekBar): Observable<Int> {
        val subject = PublishSubject.create<Int>()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) subject.onNext(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        return subject
    }

    fun fromCircleSeekBar(seekBar: CircularSeekBar): Observable<Int> {
        val subject = PublishSubject.create<Int>()
        seekBar.setOnSeekBarChangeListener(object : CircularSeekBar.OnCircularSeekBarChangeListener{
            override fun onProgressChanged(circularSeekBar: CircularSeekBar?, progress: Float, fromUser: Boolean) {
                if (fromUser) subject.onNext(progress.toInt())
            }

            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {}

            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {}

        })
        return subject
    }

}