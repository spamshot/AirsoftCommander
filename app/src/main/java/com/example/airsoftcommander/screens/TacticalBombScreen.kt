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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random

@Composable
fun TacticalBombScreen(navController: NavController) {

    var diffuseCodeText by remember { mutableStateOf("") } //My code for disarming bomb
    var isCodeCorrect by remember { mutableStateOf(false) } //Check if code is correct
    var isCodeCorrectText by remember { mutableStateOf("") } //Holder for changing display text
    var validCode  by remember { mutableStateOf("") } //Correct code for disarming bomb, use with diffuseCodeText and randomNumber
    var diffuseCodeLength by remember { mutableStateOf("") } //Used for getting length of diffuse code
    var codeColor by remember { mutableStateOf(Color.Black) }

    var showBombSettings by remember { mutableStateOf(true) }

    var armingText by remember { mutableStateOf("") } //Arming time in seconds
    var bombTimerText by remember { mutableStateOf("") } //Bomb timer in minutes
    var guessPenaltyText by remember { mutableStateOf("") } //Penalty in seconds


    var randomNumber by remember { mutableStateOf(0) }//Random number for diffuse code, use with diffuseCodeLength and validCode

    Column(modifier = Modifier.fillMaxSize()) {
        if (showBombSettings) {
            BombSettings(
                diffuseCodeLength = diffuseCodeLength,
                bombTimerText = bombTimerText,
                guessPenaltyText = guessPenaltyText,
                armingText = armingText,
                onDiffuseCodeLengthChange = { newValue -> diffuseCodeLength = newValue},
                onBombTimerChange = { newValue -> bombTimerText = newValue},
                onGuessPenaltyChange = { newValue -> guessPenaltyText = newValue},
                onArmingTimeChange = { newValue -> armingText = newValue},
                onStartBombClick = { armingTime, bombTimer, guessPenalty ->
                    if (armingTime.isNotEmpty() && bombTimer.isNotEmpty() && guessPenalty.isNotEmpty() && diffuseCodeLength.isNotEmpty()
                        && diffuseCodeLength.toInt() != 0 && diffuseCodeLength.toInt() <= 15) {
                        showBombSettings = false
                        for (i in 1..diffuseCodeLength.toInt()){
                            randomNumber = Random.nextInt(0, 9)
                            randomNumber.toString()
                            validCode += randomNumber
                            Log.d("Validation", "Random number checker $randomNumber")
                        }
                    }else{
                            Log.d("Validation", "All fields must be filled")
                    }
                }
            )
        }
        ArmingTimer(armingText)//Shows Arming timer from arming card
        Detonation(bombTimerText)//Shows timer from detonation card
        Penalty(guessPenaltyText)//Shows penalty card
        DiffuseCode(diffuseCodeText,isCodeCorrect,validCode,isCodeCorrectText) //Shows diffuse code card


        Column (modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.Bottom){

            RandomKeyboard(diffuseCodeText = diffuseCodeText, onItemClicked = { item ->
                when {
                    item == "Go" -> { //Check when "Go" button is pressed
                        isCodeCorrect = diffuseCodeText.length == validCode.length && validCode.contains(
                                diffuseCodeText) //Check to see if code is right
                        if (isCodeCorrect) {
                            isCodeCorrectText = "Correct!"
                            codeColor = Color.Green
                        } else {
                            isCodeCorrectText = "Incorrect!"
                            codeColor = Color.Red
                        }
                        diffuseCodeText = ""
                    }
                    item == "R" -> { //Check when "R" button is pressed, R = reset
                        diffuseCodeText = ""
                    }
                    diffuseCodeText.length == validCode.length + 1 -> {
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
    onStartBombClick: (String, String, String) -> Unit) { // For Settings Page, Others are display

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
    ){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()) {

            Text(text = "Tactical Bomb Settings", textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge)

            Row(modifier = Modifier.fillMaxWidth()){ //Arming time Row
                Text(text = "Arming Time:",
                    modifier = Modifier
                        .padding(top = 30.dp, end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(value = armingText,
                    onValueChange = { if (it.length <= 3) onArmingTimeChange(it)},
                    label = { Text(text = "Time seconds")},
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            Row(modifier = Modifier.fillMaxWidth()){
                Text(text = "Bomb Timer:",
                    modifier = Modifier
                        .padding(top = 30.dp, end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(
                    value = bombTimerText,
                    onValueChange = { if (it.length <= 2) onBombTimerChange(it)},
                    label = { Text(text = "Time minutes")},
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            Row(modifier = Modifier.fillMaxWidth()){
                Text(text = "Wrong guess penalty:",
                    modifier = Modifier
                        .padding(top = 30.dp, end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(
                    value = guessPenaltyText,
                    onValueChange = { if (it.length <= 2) onGuessPenaltyChange(it)},
                    label = { Text(text = "Penalty seconds")},
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Row(modifier = Modifier.fillMaxWidth()){
                Text(text = "Diffuse code digits:",
                    modifier = Modifier
                        .padding(top = 30.dp, end = 10.dp)
                        .weight(1f),
                )
                OutlinedTextField(
                    value = diffuseCodeLength.toString(),
                    onValueChange = { newValue ->
                        if (newValue.length <= 2){
                            onDiffuseCodeLengthChange(newValue)
                        }
                    },
                    label = { Text(text = "Code Length")},
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Bottom) {
                Button(onClick = {
                    onStartBombClick(bombTimerText, guessPenaltyText, armingText) //Sets the values of the variables
                },
                    modifier = Modifier.fillMaxWidth()){
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
            Text(text = armingText, fontSize = 20.sp,textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),)
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
            Text(text = bombTimerText, fontSize = 26.sp,textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),)
        }
    }
}

@Composable
fun Penalty(guessPenaltyText:String){ //After setting, card displays Penalty Time
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(3.dp, Color.Yellow),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 4.dp),
    ){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text(text = "Penalty",fontSize = 28.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge)
            Text(text = guessPenaltyText, fontSize = 20.sp,textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),)
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

            Text(text = validCode, fontSize = 20.sp,textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),)

            Text(text = "My Code: $diffuseCodeText", fontSize = 20.sp,textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),)
            Text(
                text = isCodeCorrectText,
                color = if (isCodeCorrect) Color.Green else Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun RandomKeyboard(diffuseCodeText: String, onItemClicked: (String) -> Unit){ //After settings set
    var keyboardArray= listOf(
        listOf("1","2","3"),
        listOf("4","5","6"),
        listOf("7","8","9"),
        listOf("R","0","Go")
    )
    var shuffledKeyboard by remember { mutableStateOf(shuffleKeyboard(keyboardArray)) }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .padding(start = 10.dp, end = 10.dp, bottom = 2.dp)
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
        ) {
            for (row in shuffledKeyboard){
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)
                ){
                    for(item in row){
                        Button(onClick = {
                            onItemClicked(item)
                            Log.d("Button", "Button clicked: $item")
                            shuffledKeyboard = shuffleKeyboard(keyboardArray)
                        }, Modifier
                            .height(72.dp)
                            .weight(1f)
                            .padding(4.dp)

                            ){
                            Text(text = item,
                                fontSize = 18.sp,)
                        }
                    }
                }
            }
        }
    }
}

fun shuffleKeyboard(keyboardArray: List<List<String>>): List<List<String>> {
    val shuffledKeyboardArray = keyboardArray.flatMap { it }.shuffled()
    return List(4) { index ->
        shuffledKeyboardArray.subList(index * 3, (index + 1) * 3)
    }
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

