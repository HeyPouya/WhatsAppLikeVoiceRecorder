package ir.heydarii.whatsapprecorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.content.res.AppCompatResources
import io.supercharge.shimmerlayout.ShimmerLayout
import omrecorder.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Devlomi on 24/08/2017.
 */

class RecordView : RelativeLayout {
    private var smallBlinkingMic: ImageView? = null
    private var basketImg: ImageView? = null
    private var counterTime: Chronometer? = null
    private var slideToCancel: TextView? = null
    private var slideToCancelLayout: ShimmerLayout? = null
    private var arrow: ImageView? = null
    private var initialX: Float = 0.toFloat()
    private var basketInitialY: Float = 0.toFloat()
    private var difX = 0f
    private var cancelBounds = DEFAULT_CANCEL_BOUNDS.toFloat()
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var mContext: Context? = null
    private var recordListener: OnRecordListener? = null
    private var isSwiped: Boolean = false
    private var isLessThanSecondAllowed = false
    private var isSoundEnabled = true
    private var RECORD_START = R.raw.record_start
    private var RECORD_FINISHED = R.raw.record_finished
    private var RECORD_ERROR = R.raw.record_error
    private var player: MediaPlayer? = null
    private var animationHelper: AnimationHelper? = null
    private var audioPath = ""
    private var recorder: Recorder? = null


    constructor(context: Context) : super(context) {
        this.mContext = context
        init(context, null, -1, -1)

    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.mContext = context
        init(context, attrs, -1, -1)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.mContext = context
        init(context, attrs, defStyleAttr, -1)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val view = View.inflate(context, R.layout.record_view_layout, null)
        addView(view)


        val viewGroup = view.parent as ViewGroup
        viewGroup.clipChildren = false

        arrow = view.findViewById(R.id.arrow)
        slideToCancel = view.findViewById(R.id.slide_to_cancel)
        smallBlinkingMic = view.findViewById(R.id.glowing_mic)
        counterTime = view.findViewById(R.id.counter_tv)
        basketImg = view.findViewById(R.id.basket_img)
        slideToCancelLayout = view.findViewById(R.id.shimmer_layout)


        hideViews(true)


        if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
            val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.RecordView,
                defStyleAttr, defStyleRes
            )


            val slideArrowResource = typedArray.getResourceId(R.styleable.RecordView_slide_to_cancel_arrow, -1)
            val slideToCancelText = typedArray.getString(R.styleable.RecordView_slide_to_cancel_text)
            val slideMarginRight =
                typedArray.getDimension(R.styleable.RecordView_slide_to_cancel_margin_right, 30f).toInt()
            val counterTimeColor = typedArray.getColor(R.styleable.RecordView_counter_time_color, -1)
            val arrowColor = typedArray.getColor(R.styleable.RecordView_slide_to_cancel_arrow_color, -1)


            val cancelBounds = typedArray.getDimensionPixelSize(R.styleable.RecordView_slide_to_cancel_bounds, -1)

            if (cancelBounds != -1)
                setCancelBounds(cancelBounds.toFloat(), false)//don't convert it to pixels since it's already in pixels


            if (slideArrowResource != -1) {
                val slideArrow = AppCompatResources.getDrawable(getContext(), slideArrowResource)
                arrow!!.setImageDrawable(slideArrow)
            }

            if (slideToCancelText != null)
                slideToCancel!!.text = slideToCancelText

            if (counterTimeColor != -1)
                setCounterTimeColor(counterTimeColor)


            if (arrowColor != -1)
                setSlideToCancelArrowColor(arrowColor)



            setMarginRight(slideMarginRight, true)

            typedArray.recycle()
        }


        animationHelper = AnimationHelper(context, basketImg!!, smallBlinkingMic!!)

    }


    private fun hideViews(hideSmallMic: Boolean) {
        slideToCancelLayout!!.visibility = View.GONE
        counterTime!!.visibility = View.GONE
        if (hideSmallMic)
            smallBlinkingMic!!.visibility = View.GONE
    }

    private fun showViews() {
        slideToCancelLayout!!.visibility = View.VISIBLE
        smallBlinkingMic!!.visibility = View.VISIBLE
        counterTime!!.visibility = View.VISIBLE
    }


    private fun isLessThanOneSecond(time: Long): Boolean {
        return time <= 1000
    }


    private fun playSound(soundRes: Int) {

        if (isSoundEnabled) {
            if (soundRes == 0)
                return

            try {
                player = MediaPlayer()
                val afd = mContext!!.resources.openRawResourceFd(soundRes) ?: return
                player!!.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                player!!.prepare()
                player!!.start()
                player!!.setOnCompletionListener { mp -> mp.release() }
                player!!.isLooping = false
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }


    }


    fun onActionDown(recordBtn: RecordButton, motionEvent: MotionEvent) {

        if (recordListener != null)
            recordListener!!.onStart()

        animationHelper!!.setStartRecorded(true)
        animationHelper!!.resetBasketAnimation()
        animationHelper!!.resetSmallMic()


        recordBtn.startScale()
        slideToCancelLayout!!.startShimmerAnimation()

        initialX = recordBtn.x

        basketInitialY = basketImg!!.y + 90

        playSound(RECORD_START)

        showViews()

        animationHelper!!.animateSmallMicAlpha()
        counterTime!!.base = SystemClock.elapsedRealtime()
        startTime = System.currentTimeMillis()
        counterTime!!.start()
        counterTime?.setOnChronometerTickListener {

            elapsedTime = System.currentTimeMillis() - startTime

            recordListener?.onTikListener(elapsedTime)
        }
        isSwiped = false
        startRecording()

    }

    private fun startRecording() {
        setupRecorder()
        recorder?.startRecording()

    }


    fun onActionMove(recordBtn: RecordButton, motionEvent: MotionEvent) {


        val time = System.currentTimeMillis() - startTime

        if (!isSwiped) {

            //Swipe To Cancel
            if (slideToCancelLayout!!.x != 0f && slideToCancelLayout!!.x <= counterTime!!.right + cancelBounds) {

                //if the time was less than one second then do not start basket animation
                if (isLessThanOneSecond(time)) {
                    hideViews(true)
                    animationHelper!!.clearAlphaAnimation(false)

                    animationHelper!!.onAnimationEnd()

                } else {
                    hideViews(false)
                    animationHelper!!.animateBasket(basketInitialY)
                }

                animationHelper!!.moveRecordButtonAndSlideToCancelBack(recordBtn, slideToCancelLayout!!, initialX, difX)

                counterTime!!.stop()
                slideToCancelLayout!!.stopShimmerAnimation()
                isSwiped = true


                animationHelper!!.setStartRecorded(false)

                if (recordListener != null)
                    recordListener!!.onCancel()


            } else {


                //if statement is to Prevent Swiping out of bounds
                if (motionEvent.rawX < initialX) {
                    recordBtn.animate()
                        .x(motionEvent.rawX)
                        .setDuration(0)
                        .start()


                    if (difX == 0f)
                        difX = initialX - slideToCancelLayout!!.x


                    slideToCancelLayout!!.animate()
                        .x(motionEvent.rawX - difX)
                        .setDuration(0)
                        .start()


                }


            }

        }
    }

    fun onActionUp(recordBtn: RecordButton) {

        elapsedTime = System.currentTimeMillis() - startTime

        if (!isLessThanSecondAllowed && isLessThanOneSecond(elapsedTime) && !isSwiped) {
            if (recordListener != null)
                recordListener!!.onLessThanSecond()

            animationHelper?.setStartRecorded(false)

            playSound(RECORD_ERROR)


        } else {

            animationHelper?.setStartRecorded(false)


            if (!isSwiped)
                playSound(RECORD_FINISHED)

        }

        if (recorder != null){
            recorder?.stopRecording()
            recorder = null
        }

        //if user has swiped then do not hide SmallMic since it will be hidden after swipe Animation
        hideViews(!isSwiped)


        if (!isSwiped)
            animationHelper?.clearAlphaAnimation(true)

        animationHelper?.moveRecordButtonAndSlideToCancelBack(recordBtn, slideToCancelLayout!!, initialX, difX)
        counterTime?.stop()
        slideToCancelLayout!!.stopShimmerAnimation()


        if (recordListener != null && !isSwiped)
            recordListener?.onFinish(elapsedTime, audioPath)

    }


    private fun setMarginRight(marginRight: Int, convertToDp: Boolean) {
        val layoutParams = slideToCancelLayout!!.layoutParams as RelativeLayout.LayoutParams
        if (convertToDp) {
            layoutParams.rightMargin = DpUtil.toPixel(marginRight.toFloat(), mContext!!).toInt()
        } else
            layoutParams.rightMargin = marginRight

        slideToCancelLayout!!.layoutParams = layoutParams
    }


    fun setOnRecordListener(recrodListener: OnRecordListener) {
        this.recordListener = recrodListener
    }

    fun setOnBasketAnimationEndListener(onBasketAnimationEndListener: OnBasketAnimationEnd) {
        animationHelper!!.setOnBasketAnimationEndListener(onBasketAnimationEndListener)
    }

    fun setSoundEnabled(isEnabled: Boolean) {
        isSoundEnabled = isEnabled
    }

    fun setLessThanSecondAllowed(isAllowed: Boolean) {
        isLessThanSecondAllowed = isAllowed
    }

    fun setSlideToCancelText(text: String) {
        slideToCancel!!.text = text
    }

    fun setSlideToCancelTextColor(color: Int) {
        slideToCancel!!.setTextColor(color)
    }

    fun setSmallMicColor(color: Int) {
        smallBlinkingMic!!.setColorFilter(color)
    }

    fun setSmallMicIcon(icon: Int) {
        smallBlinkingMic!!.setImageResource(icon)
    }

    fun setSlideMarginRight(marginRight: Int) {
        setMarginRight(marginRight, true)
    }


    fun setCustomSounds(startSound: Int, finishedSound: Int, errorSound: Int) {
        //0 means do not play sound
        RECORD_START = startSound
        RECORD_FINISHED = finishedSound
        RECORD_ERROR = errorSound
    }

    fun getCancelBounds(): Float {
        return cancelBounds
    }

    fun setCancelBounds(cancelBounds: Float) {
        setCancelBounds(cancelBounds, true)
    }

    //set Chronometer color
    fun setCounterTimeColor(color: Int) {
        counterTime!!.setTextColor(color)
    }

    fun setSlideToCancelArrowColor(color: Int) {
        arrow!!.setColorFilter(color)
    }


    private fun setCancelBounds(cancelBounds: Float, convertDpToPixel: Boolean) {
        val bounds = if (convertDpToPixel) DpUtil.toPixel(cancelBounds, mContext!!) else cancelBounds
        this.cancelBounds = bounds
    }

    companion object {

        val DEFAULT_CANCEL_BOUNDS = 8 //8dp
    }

    private fun setupRecorder() {
        recorder = OmRecorder.wav(
            PullTransport.Default(mic(), PullTransport.OnAudioChunkPulledListener { }), file()
        )
    }

    private fun mic(): PullableSource {
        return PullableSource.Default(
            AudioRecordConfig.Default(
                MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                AudioFormat.CHANNEL_IN_MONO, 44100
            )
        )
    }

    @NonNull
    private fun file(): File {
        @SuppressLint("SimpleDateFormat") val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val file = File(Environment.getExternalStorageDirectory().absolutePath, "$timeStamp.wav")
        audioPath = file.path
        return file
    }

}


