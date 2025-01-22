package com.example.airsoftcommander.screens

import com.example.airsoftcommander.R
import android.content.Context
import androidx.compose.runtime.*
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.airsoftcommander.model.TimerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.text.contains


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

    var isPenaltyBlinking by mutableStateOf(false)


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
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    var diffuseCodeText by remember { mutableStateOf("") }
    var isCodeCorrect by remember { mutableStateOf(false) }
    var isCodeCorrectText by remember { mutableStateOf("") }
    var codeColor by remember { mutableStateOf(Color.Black) }


    var timeLeft = timerViewModel.timeLeft.value
    val isTimerRunning = timerViewModel.isTimerRunning.value
    val isTimerFinished = timerViewModel.isTimerFinished.value

    val isBombArmed = bombViewModel.isBombArmed
    val isBombDetonated = bombViewModel.isBombDetonated
    val isBombDefused = bombViewModel.isBombDefused
    val isPenaltyActive = bombViewModel.isPenaltyActive
    val isArmingActive = bombViewModel.isArmingActive
    var isGameOver by remember { mutableStateOf(false)}


    //Sound stuff
//    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    LaunchedEffect(isArmingActive) {
        Log.d("Arming", "isArmingActive changed to: $isArmingActive")
        if (isArmingActive) {
            Log.d("Sound", "Playing arming sound")
            soundManager.playArming()
        } else {
            soundManager.stopArming()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

                if (bombViewModel.showBombSettings) {
                    BombSettings(
                        diffuseCodeLength = bombViewModel.diffuseCodeLength,
                        bombTimerText = bombViewModel.bombTimerText,
                        guessPenaltyText = bombViewModel.guessPenaltyText,
                        armingText = bombViewModel.armingText,
                        onDiffuseCodeLengthChange = { newValue ->
                            bombViewModel.diffuseCodeLength = newValue
                        },
                        onBombTimerChange = { newValue -> bombViewModel.bombTimerText = newValue },
                        onGuessPenaltyChange = { newValue ->
                            bombViewModel.guessPenaltyText = newValue
                        },
                        onArmingTimeChange = { newValue -> bombViewModel.armingText = newValue },
                        onStartBombClick = { armingTime, bombTimer, guessPenalty ->

                            if (armingTime.isNotEmpty() && bombTimer.isNotEmpty() && guessPenalty.isNotEmpty() && bombViewModel.diffuseCodeLength.isNotEmpty()
                            ) {
                                if (bombTimer.toInt() > 60 || bombTimer.toInt() <= 0) { //Detonation timer, we want the error
                                    Log.d("Validation", "Error Bomb Timer input, 1 to 60 ")
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Error Bomb Timer 1 to 60",
                                            duration = SnackbarDuration.Short
                                        )
                                    }

                                }else{
                                    if (armingTime.toInt() > 30) {
                                        Log.d("Validation", "Error Arming Time more than 30")
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Error Arming Time more than 30",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }else{
                                        if (bombViewModel.diffuseCodeLength.toInt() != 0 && bombViewModel.diffuseCodeLength.toInt() <= 15) {
                                            bombViewModel.startBomb()
                                            // timerViewModel.resetTimer()
                                            timerViewModel.startArmingTimer(bombViewModel.armingText.toInt()) //Passing arming time to timer
                                        }else{
                                            Log.d("Validation", "Error Diffuse Code 1 to 15")
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Error Diffuse Code 1 to 15",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                                }

                            } else {
                                Log.d("Validation", "All fields must be filled")
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Bad setting input",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                    )
                } else {

                        if (isArmingActive) {
                            ArmingTimer(timeLeft.toString())
                            Log.d("Arming", "isArmingActive is true zzzzz")
                            if (isTimerFinished) {

                                timerViewModel.startTimer(bombViewModel.bombTimerText.toInt()) //passing bomb timer to timer
                                bombViewModel.isArmingActive = false
                            }
                        }
                    if (isBombArmed && !isArmingActive) {
                        if (!isTimerRunning) { //Works but gets called 2x here
                            Log.d("Timer", "Timer not running")
                        }
                        soundManager.playBombBeeping()

                        Detonation(timeLeft.toString()) //shows Detonation with timer on end of arming
                        if (isBombDefused) {
                            soundManager.stopBombBeeping()
                            soundManager.playGameOwn()
                            LaunchedEffect(isBombDefused) {
                                navController.navigate("end_screen/true"){
                                    popUpTo("bomb_screen") { inclusive = true }
                                }
                            }
                        }

                        if (isTimerFinished) {
                            bombViewModel.isBombDetonated = true
                            if (isBombDetonated) {
                                soundManager.stopErrorKeypad()
                                soundManager.stopBombBeeping()
                                soundManager.playBombDetonation()
                                LaunchedEffect(isBombDetonated) {
                                    navController.navigate("end_screen/false"){
                                        popUpTo("bomb_screen") { inclusive = true }
                                    }
                                }
                            }
                        }
                        Penalty(
                            bombViewModel.guessPenaltyText,
                            timeLeft,
                            bombViewModel.isPenaltyBlinking,
                            bombViewModel
                        ) // shows Penalty on end of arming
                        DiffuseCode(
                            diffuseCodeText,
                            isCodeCorrect,
                            bombViewModel.validCode,
                            isCodeCorrectText
                        ) //shows DiffuseCode on end of arming
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 40.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            RandomKeyboard(onItemClicked = { item -> //Shows keyboard on start of arming
                                when {
                                    item == "Go" -> {
                                        isCodeCorrect =
                                            diffuseCodeText.length == bombViewModel.validCode.length && bombViewModel.validCode.contains(
                                                diffuseCodeText
                                            )
                                        if (isCodeCorrect) {
                                            isCodeCorrectText = "Correct!"
                                            codeColor = Color.Green
                                            bombViewModel.isBombDefused = true
                                            timerViewModel.stopTimer() //todo Looking here
                                        } else {
                                            isCodeCorrectText = "Incorrect!"
                                            codeColor = Color.Red
                                            timerViewModel.startPenaltyTimer(bombViewModel.guessPenaltyText.toInt()) //calls penalty timer
                                            bombViewModel.isPenaltyActive = true
                                            bombViewModel.isPenaltyBlinking = true
                                            soundManager.errorKeypadPlayer()

                                        }
                                        diffuseCodeText = ""
                                    }

                                    item == "R" -> { //Resets diffuse code
                                        diffuseCodeText = ""
                                        bombViewModel.isPenaltyActive = true
                                        timerViewModel.startPenaltyTimer(bombViewModel.guessPenaltyText.toInt()) //calls penalty timer
                                        bombViewModel.isPenaltyBlinking = true
                                        soundManager.errorKeypadPlayer()
                                    }

                                    diffuseCodeText.length == bombViewModel.validCode.length + 1 -> { //todo Think error here, need penalty
                                        diffuseCodeText = ""
                                        timerViewModel.startPenaltyTimer(bombViewModel.guessPenaltyText.toInt()) //calls penalty timer
                                        bombViewModel.isPenaltyActive = true
                                        bombViewModel.isPenaltyBlinking = true
                                        soundManager.errorKeypadPlayer()
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
            .padding(top = 12.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
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
                    onValueChange = { it -> if(it.all { it.isDigit() }){
                        if (it.length <= 3) onArmingTimeChange(it) }
                    },
                    label = { Text(text = "Time seconds") },
                    placeholder = { Text(text = "0 to 30") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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
                    onValueChange = { it -> if(it.all { it.isDigit() }){
                        if (it.length <= 2) onBombTimerChange(it) }
                    },
                    label = { Text(text = "Time minutes") },
                    placeholder = { Text(text = "1 to 60") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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
                    onValueChange = { it -> if(it.all { it.isDigit() }){
                        if (it.length <= 2) onGuessPenaltyChange(it) }
                    },
                    label = { Text(text = "Penalty seconds") },
                    placeholder = { Text(text = "0 to 99") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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
                    onValueChange = { newValue -> if(newValue.all { it.isDigit() }){
                        if (newValue.length <= 2) {
                            onDiffuseCodeLengthChange(newValue)
                        }
                    }
                    },
                    placeholder = { Text(text = "1 to 15") },
                    label = { Text(text = "Code Length") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(onClick = {
                    onStartBombClick(armingText, bombTimerText, guessPenaltyText) //Sets the values of the variables
                },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Start Bomb",fontSize = 30.sp)
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
            .padding(top = 22.dp, start = 10.dp, end = 10.dp, bottom = 4.dp),
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
            .padding(top = 22.dp, start = 10.dp, end = 10.dp, bottom = 4.dp),
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
fun Penalty(guessPenaltyText: String, timeLeft: Int, isBlinking: Boolean, bombViewModel: BombViewModel) {

    val boarderColor by animateColorAsState(
        targetValue = if (isBlinking) Color.Red else Color.Yellow,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(150),
            repeatMode  = RepeatMode.Reverse
        ),
        finishedListener = {
            bombViewModel.isPenaltyBlinking = false

        }
    )

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(3.dp, boarderColor),
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
                text = "Penalty", //$timeLeft
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
                text = validCode, fontSize = 28.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "My Code: $diffuseCodeText", fontSize = 23.sp, textAlign = TextAlign.Center,
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
fun RandomKeyboard(onItemClicked: (String) -> Unit) {
    val keyboardArray = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("R", "0", "Go")
    )
    var shuffledKeyboard by remember { mutableStateOf(shuffleKeyboard(keyboardArray)) }
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

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
//                                Log.d("Button", "Button clicked: $item")
                                shuffledKeyboard = shuffleKeyboard(keyboardArray) // Shuffle on click\
                                soundManager.playKeypad()

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

class SoundManager (private val context: Context){
    private var keypadPlayer: MediaPlayer? = null
    private var errorKeypadPlayer: MediaPlayer? = null

    private var armingMediaPlayer: MediaPlayer? = null
    private var playBombBeeping: MediaPlayer? = null
    private var playBombDetonation: MediaPlayer? = null
    private var playGameOwn: MediaPlayer? = null

    fun playKeypad() {
        try {
            // Release the previous keypadMediaPlayer if it exists
            keypadPlayer?.release()
            keypadPlayer = null

            // Create a new MediaPlayer for the keypad sound
            keypadPlayer = MediaPlayer.create(context, R.raw.keypadz)

            // Check if MediaPlayer was created successfully
            if (keypadPlayer != null) {
                keypadPlayer?.isLooping = false // Loop the keypad sound
                keypadPlayer?.start()
            } else {
                Log.e("SoundManager", "Failed to create MediaPlayer for keypad sound.")
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing keypad sound: ${e.message}")
        }
    }

    fun stopKeypad() {
        keypadPlayer?.release()
        keypadPlayer = null
    }
    fun errorKeypadPlayer() {
        try {
            // Release the previous keypadMediaPlayer if it exists
            errorKeypadPlayer?.release()
            errorKeypadPlayer = null

            // Create a new MediaPlayer for the keypad sound
            errorKeypadPlayer = MediaPlayer.create(context, R.raw.errorsound)

            // Check if MediaPlayer was created successfully
            if (errorKeypadPlayer != null) {
                errorKeypadPlayer?.isLooping = false // Loop the keypad sound
                errorKeypadPlayer?.start()
            } else {
                Log.e("SoundManager", "Failed to create MediaPlayer for ErrorKeypad sound.")
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing ErrorKeypad sound: ${e.message}")
        }
    }

    fun stopErrorKeypad() {
        errorKeypadPlayer?.release()
        errorKeypadPlayer = null
    }


    fun playArming() {
        try {
            // Release the previous armingMediaPlayer if it exists
            armingMediaPlayer?.release()
            armingMediaPlayer = null

            // Create a new MediaPlayer for the arming sound
            armingMediaPlayer = MediaPlayer.create(context, R.raw.siren)

            // Check if MediaPlayer was created successfully
            if (armingMediaPlayer != null) {
                armingMediaPlayer?.isLooping = true // Loop the arming sound
                armingMediaPlayer?.start()
            } else {
                Log.e("SoundManager", "Failed to create MediaPlayer for arming sound.")
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing arming sound: ${e.message}")
        }
    }
    fun stopArming() {
        armingMediaPlayer?.release()
        armingMediaPlayer = null
    }

    fun playBombBeeping() {
        try {
            // Release the previous Bomb Beeping MediaPlayer if it exists
            playBombBeeping?.release()
            playBombBeeping = null

            // Create a new MediaPlayer for the Bomb Beeping sound
            playBombBeeping = MediaPlayer.create(context, R.raw.bombbeep)

            // Check if MediaPlayer was created successfully
            if (playBombBeeping != null) {
                playBombBeeping?.isLooping = true // Loop the Bomb Beeping sound
                playBombBeeping?.start()
            } else {
                Log.e("SoundManager", "Failed to create MediaPlayer for Bomb Beeping sound.")
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing Bomb Beeping sound: ${e.message}")
        }
    }

    fun stopBombBeeping() {
        playBombBeeping?.release()
        playBombBeeping = null
    }

    fun playBombDetonation() {
        try {
            // Release the previous Bomb Detonation MediaPlayer if it exists
            playBombDetonation?.release()
            playBombDetonation = null

            // Create a new MediaPlayer for the keypad sound
            playBombDetonation = MediaPlayer.create(context, R.raw.explosionbomb)

            // Check if MediaPlayer was created successfully
            if (playBombDetonation != null) {
                playBombDetonation?.isLooping = false // Loop the Bomb Detonation sound
                playBombDetonation?.start()
            } else {
                Log.e("SoundManager", "Failed to create MediaPlayer for Bomb Detonation sound.")
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing Bomb Detonation sound: ${e.message}")
        }
    }

    fun stopBombDetonation() {
        playBombDetonation?.release()
        playBombDetonation = null
    }

    fun playGameOwn() {
        try {
            // Release the previous Game own MediaPlayer if it exists
            playGameOwn?.release()
            playGameOwn = null

            // Create a new MediaPlayer for the Game own sound
            playGameOwn = MediaPlayer.create(context, R.raw.gameown)

            // Check if MediaPlayer was created successfully
            if (playGameOwn != null) {
                playGameOwn?.isLooping = false // Loop the Game won sound
                playGameOwn?.start()
            } else {
                Log.e("SoundManager", "Failed to create MediaPlayer for Bomb Beeping sound.")
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing Bomb Beeping sound: ${e.message}")
        }
    }

    fun stopGameOwn() {
        playGameOwn?.release()
        playGameOwn = null
    }
}






//Sound files from https://www.pond5.com/
//Sound files protected by a copyright
//ref link https://www.pond5.com?ref=kylanhill800