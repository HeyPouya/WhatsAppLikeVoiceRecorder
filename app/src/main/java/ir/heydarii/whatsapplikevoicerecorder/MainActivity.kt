package ir.heydarii.whatsapplikevoicerecorder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ir.heydarii.whatsapprecorder.OnRecordListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        record_button.setRecordView(record_view)

        record_view.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                Log.d("ACTION", "Start")
            }

            override fun onCancel() {
                Log.d("ACTION", "Cancel")

            }

            override fun onFinish(recordTime: Long, audioPath: String) {
                Log.d("ACTION", "finish $recordTime")

            }

            override fun onLessThanSecond() {
                Log.d("ACTION", "Less")

            }

            override fun onTikListener(recordTime: Long) {
                Log.d("ACTION", "Tick $recordTime")

            }
        })


    }
}
