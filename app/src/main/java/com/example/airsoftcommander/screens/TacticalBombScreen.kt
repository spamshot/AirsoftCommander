package com.example.airsoftcommander.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.airsoftcommander.model.TimerViewModel
import kotlin.random.Random



class BombViewModel : ViewModel() {
    var diffuseCodeLength by mutableStateOf("")
    var bombTimerText by mutableStateOf("")
    var guessPenaltyText by mutableStateOf("")
    var armingText by mutableStateOf("")
    var validCode by mutableStateOf("")
    var showBombSettings by mutableStateOf(true)
    var isBombArmed by mutableStateOf(false)
    var isBombDetonated by mutableStateOf(false)
    var isBombDefused by mutableStateOf(false)
    var isPenaltyActive by mutableStateOf(false)
    var isArmingActive by mutableStateOf(false)

    fun generateValidCode() {
        validCode = ""
        for (i in 1..diffuseCodeLength.toInt()) {
            val randomNumber = Random.nextInt(0, 9)
            validCode += randomNumber.toString()
        }
    }

    fun startBomb() {
        generateValidCode()
        showBombSettings = false
        isBombArmed = true
        isArmingActive = true
    }

    fun resetBomb() {
        showBombSettings = true
        isBombArmed = false
        isBombDetonated = false
        isBombDefused = false
        isPenaltyActive = false
        isArmingActive = false
        diffuseCodeLength = ""
        bombTimerText = ""
        guessPenaltyText = ""
        armingText = ""
        validCode = ""
    }
}

@Composable
fun TacticalBombScreen(
    navController: NavController,
    timerViewModel: TimerViewModel = viewModel(),
    bombViewModel: BombViewModel = viewModel()
) {
    var diffuseCodeText by remember { mutableStateOf("") }
    var isCodeCorrect by remember { mutableStateOf(false) }
    var isCodeCorrectText by remember { mutableStateOf("") }
    var codeColor by remember { mutableStateOf(Color.Black) }

    val timeLeft = timerViewModel.timeLeft.value
    val isTimerRunning = timerViewModel.isTimerRunning.value
    val isTimerFinished = timerViewModel.isTimerFinished.value

    val isBombArmed = bombViewModel.isBombArmed
    val isBombDetonated = bombViewModel.isBombDetonated
    val isBombDefused = bombViewModel.isBombDefused
    val isPenaltyActive = bombViewModel.isPenaltyActive
    val isArmingActive = bombViewModel.isArmingActive

    Column(modifier = Modifier.fillMaxSize()) {
        if (bombViewModel.showBombSettings) {
            BombSettings(
                diffuseCodeLength = bombViewModel.diffuseCodeLength,
                bombTimerText = bombViewModel.bombTimerText,
                guessPenaltyText = bombViewModel.guessPenaltyText,
                armingText = bombViewModel.armingText,
                onDiffuseCodeLengthChange = { newValue -> bombViewModel.diffuseCodeLength = newValue },
                onBombTimerChange = { newValue -> bombViewModel.bombTimerText = newValue },
                onGuessPenaltyChange = { newValue -> bombViewModel.guessPenaltyText = newValue },
                onArmingTimeChange = { newValue -> bombViewModel.armingText = newValue },
                onStartBombClick = { armingTime, bombTimer, guessPenalty ->
                    if (armingTime.isNotEmpty() && bombTimer.isNotEmpty() && guessPenalty.isNotEmpty() && bombViewModel.diffuseCodeLength.isNotEmpty()
                        && bombViewModel.diffuseCodeLength.toInt() != 0 && bombViewModel.diffuseCodeLength.toInt() <= 15
                    ) {
                        bombViewModel.startBomb()
                        timerViewModel.resetTimer()
                        timerViewModel.startArmingTimer(bombViewModel.armingText.toInt())
                    } else {
                        Log.d("Validation", "All fields must be filled")
                    }
                }
            )
        } else {
            if (isArmingActive) {
                ArmingTimer(timeLeft.toString())
                if (isTimerFinished){
                    timerViewModel.resetTimer()
                    timerViewModel.startTimer(bombViewModel.bombTimerText.toInt())
                    bombViewModel.isArmingActive = false
                }
            }
            if (isBombArmed && !isArmingActive) {
                Detonation(timeLeft.toString())
                if (isTimerFinished){
                    bombViewModel.isBombDetonated = true
                }
            }
            if (isPenaltyActive) {
                Penalty(bombViewModel.guessPenaltyText, timeLeft) // Corrected call
            }
            if (isBombDetonated) {
                Text(text = "Bomb Detonated!", color = Color.Red, fontSize = 30.sp)
            }
            if (isBombDefused) {
                Text(text = "Bomb Defused!", color = Color.Green, fontSize = 30.sp)
            }
            DiffuseCode(diffuseCodeText, isCodeCorrect, bombViewModel.validCode, isCodeCorrectText)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                RandomKeyboard(onItemClicked = { item ->
                    when {
                        item == "Go" -> {
                            isCodeCorrect = diffuseCodeText.length == bombViewModel.validCode.length && bombViewModel.validCode.contains(
                                diffuseCodeText
                            )
                            if (isCodeCorrect) {
                                isCodeCorrectText = "Correct!"
                                codeColor = Color.Green
                                bombViewModel.isBombDefused = true
                                timerViewModel.stopTimer()
                            } else {
                                isCodeCorrectText = "Incorrect!"
                                codeColor = Color.Red
                                timerViewModel.startPenaltyTimer(bombViewModel.guessPenaltyText.toInt())
                                bombViewModel.isPenaltyActive = true
                            }
                            diffuseCodeText = ""
                        }

                        item == "R" -> {
                            diffuseCodeText = ""
                        }

                        diffuseCodeText.length == bombViewModel.validCode.length + 1 -> {
                            diffuseCodeText = ""
                        }

                        else -> {
                            diffuseCodeText += item
                        }
                    }
                    Log.d("Button", "Button clicked: $item")
                })
            }
        }
    }
}

@Composable
fun BombSettings(
    diffuseCodeLength: String,
    bombTimerText: String,
    guessPenaltyText: String,
    armingText: String,
    onDiffuseCodeLengthChange: (String) -> Unit,
    onBombTimerChange: (String) -> Unit,
    onGuessPenaltyChange: (String) -> Unit,
    onArmingTimeChange: (String) -> Unit,
    onStartBombClick: (String, String, String) -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {

            Text(
                text = "Tactical Bomb Settings", textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge
            )

            Row(modifier = Modifier.fillMaxWidth()) { //Arming time Row
                Text(
                    text = "Arming Time:",
                    modifier = Modifier
                        .padding(top = 30.dp, end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(
                    value = armingText,
                    onValueChange = { if (it.length <= 3) onArmingTimeChange(it) },
                    label = { Text(text = "Time seconds") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Bomb Timer:",
                    modifier = Modifier
                        .padding(top = 30.dp, end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(
                    value = bombTimerText,
                    onValueChange = { if (it.length <= 2) onBombTimerChange(it) },
                    label = { Text(text = "Time minutes") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Wrong guess penalty:",
                    modifier = Modifier
                        .padding(top = 30.dp, end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(
                    value = guessPenaltyText,
                    onValueChange = { if (it.length <= 2) onGuessPenaltyChange(it) },
                    label = { Text(text = "Penalty seconds") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Diffuse code digits:",
                    modifier = Modifier
                        .padding(top = 30.dp, end = 10.dp)
                        .weight(1f),
                )
                OutlinedTextField(
                    value = diffuseCodeLength.toString(),
                    onValueChange = { newValue ->
                        if (newValue.length <= 2) {
                            onDiffuseCodeLengthChange(newValue)
                        }
                    },
                    label = { Text(text = "Code Length") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Bottom
            ) {
                Button(onClick = {
                    onStartBombClick(armingText, bombTimerText, guessPenaltyText) //Sets the values of the variables
                },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Start Bomb")
                }
            }
        }
    }
}


@Composable
fun ArmingTimer(armingText:String){ //After setting, card displays arming time
    OutlinedCard (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(3.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 4.dp),
    ){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text(text = "Bomb Arming",fontSize = 28.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge)
            Text(
                text = armingText, fontSize = 20.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun Detonation(bombTimerText:String){ //After setting, card displays timer
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(3.dp, Color.Red),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 42.dp, start = 10.dp, end = 10.dp, bottom = 4.dp),
    ){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text(text = "Detonation",fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold)
            Text(
                text = bombTimerText, fontSize = 26.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun Penalty(guessPenaltyText: String, timeLeft: Int) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(3.dp, Color.Yellow),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Penalty $timeLeft",
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Penalty time: $guessPenaltyText",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun DiffuseCode(diffuseCodeText: String, isCodeCorrect: Boolean, validCode: String, isCodeCorrectText: String){ //After setting, card Diffuse Code Card Info

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(5.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 2.dp),
    ){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text(text = "Diffuse Code",fontSize = 23.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge)

            Text(
                text = validCode, fontSize = 20.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "My Code: $diffuseCodeText", fontSize = 20.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = isCodeCorrectText,
                color = if (isCodeCorrect) Color.Green else Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun RandomKeyboard(onItemClicked: (String) -> Unit ) {
    val keyboardArray = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("R", "0", "Go")
    )
    var shuffledKeyboard by remember { mutableStateOf(shuffleKeyboard(keyboardArray)) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .padding(start = 10.dp, end = 10.dp, bottom = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            for (row in shuffledKeyboard) {
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    for (item in row) {
                        Button(
                            onClick = {
                                onItemClicked(item)
                                Log.d("Button", "Button clicked: $item")
                                shuffledKeyboard = shuffleKeyboard(keyboardArray) // Shuffle on click
                            },
                            Modifier
                                .height(72.dp)
                                .weight(1f)
                                .padding(4.dp)

                        ) {
                            Text(
                                text = item,
                                fontSize = 18.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

fun shuffleKeyboard(keyboard: List<List<String>>): List<List<String>> {
    val allItems = keyboard.flatten().toMutableList()
    val shuffledItems = allItems.shuffled()
    return shuffledItems.chunked(3)
}


/*
---- todo checklist <----------
Set inputs to Timers
1. Arming timer
    Hide Arming card
    Show Other cards
2. Detonation timer
    End Screen
    Button to return to games page
3. Guess penalty
    Subtract penalty from timer

Handel inputs in settings page
 1. Max input for diffuse code 15 ---\/
    Need value(15), doing length right now, need both ---\/
    Need input wronging if over

Penalty card flash color when wrong input

 */

