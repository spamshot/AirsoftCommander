package com.example.airsoftcommander.model

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    private val _timeLeft = mutableStateOf(0)
    var timeLeft: State<Int> = _timeLeft

    private val _isTimerRunning = mutableStateOf(false)
    val isTimerRunning: State<Boolean> = _isTimerRunning

    private val _isTimerFinished = mutableStateOf(false)
    val isTimerFinished: State<Boolean> = _isTimerFinished

    private var timer: CountDownTimer? = null

    fun startTimer(minutes: Int) {
        val totalTimeInMillis = minutes * 60 * 1000L
        timer?.cancel()
        timer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished / 1000).toInt()
                _isTimerRunning.value = true
                _isTimerFinished.value = false
            }

            override fun onFinish() {
                _isTimerRunning.value = false
                _isTimerFinished.value = true
            }
        }.start()
    }

    fun startArmingTimer(seconds: Int) { //seconds is arming time that we pass
        val totalTimeInMillis = seconds * 1000L
        timer?.cancel()
        timer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished / 1000).toInt()
                _isTimerRunning.value = true
                _isTimerFinished.value = false
            }

            override fun onFinish() {
                _isTimerRunning.value = false
                _isTimerFinished.value = true
            }
        }.start()
    }

    fun startPenaltyTimer(toInt: Int) { //toInt is the penalty time.
        if (!isTimerRunning.value) return

        // Calculate new time by subtracting 30 seconds
        val remainingMillis = (_timeLeft.value * 1000L) - (toInt * 1000L) //todo update to input of penalty time
        if (remainingMillis <= 0) {
            timer?.cancel()
            _timeLeft.value = 0
            _isTimerRunning.value = false
            _isTimerFinished.value = true
            return
        }

        // Cancel current timer and start new one with reduced time
        timer?.cancel()
        timer = object : CountDownTimer(remainingMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished / 1000).toInt()
                _isTimerRunning.value = true
                _isTimerFinished.value = false
            }

            override fun onFinish() {
                _isTimerRunning.value = false
                _isTimerFinished.value = true
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        _isTimerRunning.value = false
    }

    fun resetTimer() {
        timer?.cancel()
        _timeLeft.value = 0
        _isTimerRunning.value = false
        _isTimerFinished.value = false
    }
}