package com.example.airsoftcommander.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel (initialTime: Int = 10): ViewModel() {
    private val _timeLeft = mutableStateOf(initialTime)// start with 10 seconds
    val timeLeft: State<Int> = _timeLeft

    private var _isTimerRunning = mutableStateOf(false)
    val isTimerRunning: State<Boolean> = _isTimerRunning

    private var _isTimerFinished = mutableStateOf(false)
    val isTimerFinished: State<Boolean> = _isTimerFinished

    private var timerJob: Job? = null
    


    fun startTimer() {
        if (_isTimerRunning.value) return // If timer is already running, do nothing
        _isTimerRunning.value = true
        _isTimerFinished.value = false

        timerJob = viewModelScope.launch {
            while(_timeLeft.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _timeLeft.value--
            }
            _isTimerRunning.value = false
            _isTimerFinished.value = true
            }
        }
    fun stopTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = false
    }
    fun resetTimer() {
       stopTimer()
        _timeLeft.value = 10 // Reset time to 10 seconds
        _isTimerFinished.value = false

    }
}