package ir.heydarii.whatsapprecorder

/**
 * Created by Devlomi on 24/08/2017.
 */

interface OnRecordListener {
    fun onStart()
    fun onCancel()
    fun onFinish(recordTime: Long, audioPath: String)
    fun onLessThanSecond()
    fun onTikListener(recordTime: Long)
}