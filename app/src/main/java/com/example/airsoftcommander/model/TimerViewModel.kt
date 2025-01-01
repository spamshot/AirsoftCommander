package com.example.airsoftcommander.model

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    private val _timeLeft = mutableStateOf(0)
    val timeLeft: State<Int> = _timeLeft

    private val _isTimerRunning = mutableStateOf(false)
    val isTimerRunning: State<Boolean> = _isTimerRunning

    private val _isTimerFinished = mutableStateOf(false)
    val isTimerFinished: State<Boolean> = _isTimerFinished

    private var timer: CountDownTimer? = null

    fun startTimer(minutes: Int) {
        val totalTimeInMillis = minutes * 60 * 1000L
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

    fun startArmingTimer(seconds: Int) {
        val totalTimeInMillis = seconds * 1000L
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

    fun startPenaltyTimer(seconds: Int) {
        val totalTimeInMillis = seconds * 1000L
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