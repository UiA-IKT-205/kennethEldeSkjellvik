package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var pauseTimer:CountDownTimer
    lateinit var startButton:Button
    lateinit var countdownDisplay:TextView
    lateinit var countdownDisplayPause:TextView
    lateinit var loopText:EditText


    var workCountDownTimeInMs = 5000L
    var pauseCountDownTimeInMs = 0L
    val timeTicks:Long = 1000L
    val oneMinute:Long = 600L
    var numberOfLoops:Int = 0
    var isCounting:Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loopText = findViewById<EditText>(R.id.editLoops)

        // Work countdown
        var workTimeInMinutes = 0
        val workSeekBar = findViewById<SeekBar>(R.id.seekBarWork)
        workSeekBar?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                    seek: SeekBar,
                    progress: Int, fromUser: Boolean
            ) {
                workTimeInMinutes = progress

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //Not in use in this code
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(this@MainActivity,
                        "Work for " + workTimeInMinutes + " minutes",
                        Toast.LENGTH_SHORT).show()
                setWorkTimer(oneMinute * workTimeInMinutes.toLong())
            }
        })

        // Pause countdown
        var pauseTimeInMinutes = 0
        val pauseSeekBar = findViewById<SeekBar>(R.id.seekBarBreak)
        pauseSeekBar?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                    seek: SeekBar,
                    progress: Int, fromUser: Boolean
            ) {
                pauseTimeInMinutes = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                //Not in use in this code
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(this@MainActivity,
                        "A " + pauseTimeInMinutes + " minute break",
                        Toast.LENGTH_SHORT).show()
                setPauseTimer(oneMinute * pauseTimeInMinutes.toLong())
            }
        })


        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener {
            numberOfLoops = loopText.text.toString().toInt()
            // If a countdown is running, you can't start another one
            if(!isCounting){
                startCountDown(it)
            }

        }
        countdownDisplay = findViewById<TextView>(R.id.countDownView)
        countdownDisplayPause = findViewById<TextView>(R.id.countDownViewPause)
    }

    fun startCountDown(v: View){
        isCounting = true

        timer = object : CountDownTimer(workCountDownTimeInMs, timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Slutt på jobbing", Toast.LENGTH_SHORT).show()
                isCounting = false
                if(workCountDownTimeInMs > 0)
                    startPauseTimer(v)

                else {
                    isCounting = false
                    timer.cancel()
                    updateCountDownDisplay(0)
                }
            }


            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }
        timer.start()
    }

    fun startPauseTimer(v: View){

        pauseTimer = object : CountDownTimer(pauseCountDownTimeInMs, timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Pausen er ferdig", Toast.LENGTH_SHORT).show()
                isCounting = false
                if(numberOfLoops > 0){
                    numberOfLoops--
                    startCountDown(v)
                }

            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplayPause(millisUntilFinished)
            }
        }
        pauseTimer.start()
    }

    fun updateCountDownDisplay(timeInMs: Long){
        countdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

    fun updateCountDownDisplayPause(timeInMs: Long){
        countdownDisplayPause.text = millisecondsToDescriptiveTime(timeInMs)
    }

    fun setWorkTimer(workInMilliseconds: Long){
        workCountDownTimeInMs = workInMilliseconds
        println(workCountDownTimeInMs)
        // Updates the worktime when it's counting down
        updateCountDownDisplay(workInMilliseconds)
    }
    fun setPauseTimer(pauseInMilliseconds: Long){
        pauseCountDownTimeInMs = pauseInMilliseconds
        // Updates the pausetime when it's counting down
        updateCountDownDisplayPause(pauseInMilliseconds)
    }


}