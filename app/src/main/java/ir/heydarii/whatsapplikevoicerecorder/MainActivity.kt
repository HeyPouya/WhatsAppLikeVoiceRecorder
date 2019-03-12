package ir.heydarii.whatsapplikevoicerecorder

import android.os.Bundle
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

            }

            override fun onCancel() {
            }

            override fun onFinish(recordTime: Long, audioPath: String) {

            }

            override fun onLessThanSecond() {
            }

            override fun onTikListener(recordTime: Long) {

            }
        })


    }
}
