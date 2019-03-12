package ir.heydarii.whatsapprecorder

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView

/**
 * Created by Devlomi on 13/12/2017.
 */

class RecordButton : AppCompatImageView, View.OnTouchListener, View.OnClickListener {

    private var scaleAnim: ScaleAnim? = null
    private var recordView: RecordView? = null
    private var isListenForRecord = true
    private var onRecordClickListener: OnRecordClickListener? = null


    fun setRecordView(recordView: RecordView) {
        this.recordView = recordView
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)


    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordButton)

            val imageResource = typedArray.getResourceId(R.styleable.RecordButton_mic_icon, -1)


            if (imageResource != -1) {
                setTheImageResource(imageResource)
            }

            typedArray.recycle()
        }


        scaleAnim = ScaleAnim(this)


        this.setOnTouchListener(this)
        this.setOnClickListener(this)


    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setClip(this)
    }

    private fun setClip(v: View) {
        if (v.parent == null) {
            return
        }

        if (v is ViewGroup) {
            v.clipChildren = false
            v.clipToPadding = false
        }

        if (v.parent is View) {
            setClip(v.parent as View)
        }
    }


    private fun setTheImageResource(imageResource: Int) {
        val image = AppCompatResources.getDrawable(context, imageResource)
        setImageDrawable(image)
    }


    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (isListenForRecord) {
            when (event.action) {

                MotionEvent.ACTION_DOWN -> recordView!!.onActionDown(v as RecordButton, event)


                MotionEvent.ACTION_MOVE -> recordView!!.onActionMove(v as RecordButton, event)

                MotionEvent.ACTION_UP -> recordView!!.onActionUp(v as RecordButton)
            }

        }
        return isListenForRecord


    }


    fun startScale() {
        scaleAnim!!.start()
    }

    fun stopScale() {
        scaleAnim!!.stop()
    }

    fun setOnRecordClickListener(onRecordClickListener: OnRecordClickListener) {
        this.onRecordClickListener = onRecordClickListener
    }


    override fun onClick(v: View) {
        if (onRecordClickListener != null)
            onRecordClickListener!!.onClick(v)
    }
}
