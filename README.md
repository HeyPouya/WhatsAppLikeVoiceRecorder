# Whatsapp-like voice recorder


[![](https://jitpack.io/v/SirLordPouya/WhatsAppLikeVoiceRecorder.svg)](https://jitpack.io/#SirLordPouya/WhatsAppLikeVoiceRecorder)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Build Status](https://travis-ci.org/SirLordPouya/WhatsAppLikeVoiceRecorder.svg?branch=master)](https://travis-ci.org/SirLordPouya/WhatsAppLikeVoiceRecorder)

<p align="center">
<img src="https://raw.githubusercontent.com/SirLordPouya/AndroidAppUpdater/master/icon.png" width="250">
</p>

This library helps you to record the voice, with the style of whatsapp.

##### It is fully integrated with Kotlin and androidX.

<p align="center">
<img src="https://raw.githubusercontent.com/SirLordPouya/WhatsAppLikeVoiceRecorder/master/voice_record.gif" width="250"> 
</p>

## Releases:

#### Current release: [![](https://jitpack.io/v/SirLordPouya/WhatsAppLikeVoiceRecorder.svg)](https://jitpack.io/#SirLordPouya/WhatsAppLikeVoiceRecorder)



## Usage:

### Permissions :
   Don't forget to get RECORD_AUDIO and WRITE_EXTERNAL_STORAGE permissions :
   
   ```
   <uses-permission android:name="android.permission.RECORD_AUDIO"/>
   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
   ```
### XML

```xml

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/parent_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">


    <ir.heydarii.whatsapprecorder.RecordView
        android:id="@+id/record_view"
        android:layout_width="0dp"
        android:layout_height="60dp"
        app:counter_time_color="@android:color/holo_red_dark"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@id/record_button"
        app:layout_constraintStart_toStartOf="parent"
        app:slide_to_cancel_arrow="@drawable/recv_ic_arrow"
        app:slide_to_cancel_arrow_color="@android:color/white"
        app:slide_to_cancel_bounds="10dp"
        app:slide_to_cancel_margin_right="10dp"
        app:slide_to_cancel_text="@string/slide_to_cancel" />

    <ir.heydarii.whatsapprecorder.RecordButton
        android:id="@+id/record_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginEnd="8dp"
        android:layout_marginBottom="8dp"
        android:background="@drawable/recv_bg_mic"
        android:scaleType="centerInside"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="1.0"
        app:layout_constraintStart_toStartOf="parent"
        app:mic_icon="@drawable/recv_ic_mic_white" />


</androidx.constraintlayout.widget.ConstraintLayout>


```


### Kotlin

```kotlin
        //IMPORTANT : If you dont do it, you'll get an exception
        record_button.setRecordView(record_view)

```

### Handling States

```kotlin
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
```

### Handle Clicks for Record Button
```kotlin

    recordButton.setListenForRecord(false)

 //ListenForRecord must be false ,otherwise onClick will not be called
         recordButton.setOnClickListener {
              Log.d("RecordButton","RECORD BUTTON CLICKED")
        }
```

### Listen for Basket Animation End

```kotlin

   recordView.setOnBasketAnimationEndListener {
               Log.d("RecordView", "Basket Animation Finished")
            }

```


Change Swipe To Cancel Bounds (when the 'Slide To Cancel' Text View gets before Counter).
default is 8dp

```kotlin
recordView.setCancelBounds(8);//dp
```

### Some Customization

```kotlin
        recordView.setSmallMicColor(Color.parseColor("#c2185b"))

        recordView.setSlideToCancelText("TEXT")

        //disable Sounds
        recordView.setSoundEnabled(false)

        //prevent recording under one Second (it's false by default)
        recordView.setLessThanSecondAllowed(false)
    
        //set Custom sounds onRecord 
        //you can pass 0 if you don't want to play sound in certain state
        recordView.setCustomSounds(R.raw.record_start,R.raw.record_finished,0)
        
        //change slide To Cancel Text Color
        recordView.setSlideToCancelTextColor(Color.parseColor("#ff0000"))
        //change slide To Cancel Arrow Color
        recordView.setSlideToCancelArrowColor(Color.parseColor("#ff0000"))
        //change Counter Time (Chronometer) color
        recordView.setCounterTimeColor(Color.parseColor("#ff0000"))

```

## Download

#### Adding the depencency

Add this to your root *build.gradle* file:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Now add the dependency to your app build.gradle file:

```groovy
   implementation 'com.github.SirLordPouya:WhatsAppLikeVoiceRecorder:1.1.5'
```

### Thanks/Credits
- [AbdulAlim Rajjoub](https://github.com/NetoDevel) for making the whole UI in [RecordView](https://github.com/3llomi/RecordView) I just improved it a bit
- [Kailash Dabhi](https://github.com/kailash09dabhi) for recording library [Om Recorder](https://github.com/kailash09dabhi/OmRecorder)

## License

```
LoadingFragment is released under the Apache License 2.0. See LICENSE for details.

Copyright (c) 2019 Pouya Heydari

```
#### <div>Library's icon has designed by <a href="https://dribbble.com/Amir-G" title="Amir Gerdakane">Amir Gerdakane</a>
