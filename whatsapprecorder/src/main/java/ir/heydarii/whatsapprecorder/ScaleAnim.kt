package ir.heydarii.whatsapprecorder

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Created by Devlomi on 13/12/2017.
 */

class ScaleAnim(private val view: View) {


    internal fun start() {
        val set = AnimatorSet()
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 2.0f)

        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 2.0f)
        set.duration = 150
        set.interpolator = AccelerateDecelerateInterpolator()
        set.playTogether(scaleY, scaleX)
        set.start()
    }

    internal fun stop() {
        val set = AnimatorSet()
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f)
        //        scaleY.setDuration(250);
        //        scaleY.setInterpolator(new DecelerateInterpolator());


        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f)
        //        scaleX.setDuration(250);
        //        scaleX.setInterpolator(new DecelerateInterpolator());


        set.duration = 150
        set.interpolator = AccelerateDecelerateInterpolator()
        set.playTogether(scaleY, scaleX)
        set.start()
    }
}