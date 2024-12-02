package com.example.airsoftcommander.screens

import androidx.compose.runtime.*
import androidx.compose.material3.*
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import com.example.airsoftcommander.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random

//todo random timer bomb game

@Composable
fun TacticalBombScreen(navController: NavController){
    //Diffuse code need Length, digits for random code, my code
    val diffuseCode by remember { mutableStateOf(0) }

    var diffuseCodeText by remember { mutableStateOf("") }
    var isCodeCorrect by remember { mutableStateOf(false) }
    val validCode = "12333"


    Column(modifier = Modifier.fillMaxSize()){
//        BombSettings(diffuseCode)
        Detonation()
//        Penalty()
        DiffuseCode(diffuseCodeText,isCodeCorrect,validCode)


        Column (modifier = Modifier.fillMaxSize()
            .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.Bottom){
            RandomKeyboard(diffuseCodeText = diffuseCodeText, onItemClicked = { item ->
                if (item == "Go") {
                    isCodeCorrect = diffuseCodeText.length == validCode.length && validCode.contains(diffuseCodeText)
                    diffuseCodeText = ""
                }else if (item == "R"){
                    diffuseCodeText = ""
                }else if(diffuseCodeText.length == validCode.length + 1){
                    diffuseCodeText = ""

                }else{
                    diffuseCodeText += item
                }
                Log.d("Button", "Button clicked: $item")

            })
        }
    }
}

@Composable
fun BombSettings(diffuseCode: Int){ // For Settings, Others are display
    var armingText by remember { mutableStateOf("") }
    var bombTimerText by remember { mutableStateOf("") }
    var guessPenaltyText by remember { mutableStateOf("") }
    var diffuseCodeText by remember { mutableStateOf("")}



    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        modifier = Modifier.fillMaxSize()
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
                        .padding(top = 30.dp,end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(value = armingText,
                    onValueChange = {if (it.length <= 3) armingText = it},
                    label = { Text(text = "Time seconds")},
                    modifier = Modifier.weight(1f),
                    singleLine = true)

            }
            Row(modifier = Modifier.fillMaxWidth()){
                Text(text = "Bomb Timer:",
                    modifier = Modifier
                        .padding(top = 30.dp,end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(value = bombTimerText,
                    onValueChange = {if (it.length <= 2) bombTimerText = it},
                    label = { Text(text = "Time minutes")},
                    modifier = Modifier.weight(1f),
                    singleLine = true)

            }
            Row(modifier = Modifier.fillMaxWidth()){
                Text(text = "Wrong guess penalty:",
                    modifier = Modifier
                        .padding(top = 30.dp,end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(value = guessPenaltyText,
                    onValueChange = { if (it.length <= 2) guessPenaltyText = it
                    },
                    label = { Text(text = "Penalty seconds")},
                    modifier = Modifier.weight(1f),
                    singleLine = true)

            }

            Row(modifier = Modifier.fillMaxWidth()){
                Text(text = "Diffuse code digits:",
                    modifier = Modifier
                        .padding(top = 30.dp,end = 10.dp)
                        .weight(1f),
                )

                OutlinedTextField(value = diffuseCodeText,
                    onValueChange = { if (it.length <= 2) diffuseCodeText = it
                    },
                    label = { Text(text = "Code Length")},
                    modifier = Modifier.weight(1f),
                    singleLine = true)

            }


            Column(modifier = Modifier.fillMaxSize().padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Bottom) {
                Button(onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()){
                    Text(text = "Start Bomb")
                }
            }
        }
    }
}


@Composable
fun Detonation(){ //After settings set
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(3.dp, Color.Red),
        modifier = Modifier.fillMaxWidth()
            .padding(top = 42.dp, start = 10.dp, end = 10.dp, bottom = 4.dp),
    ){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text(text = "Detonation",fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold)
            Text(text = "25:40", fontSize = 26.sp,textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),)
        }
    }
}

@Composable
fun Penalty(){ //After settings set
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(3.dp, Color.Yellow),
        modifier = Modifier.fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 4.dp),
    ){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text(text = "Penalty",fontSize = 28.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge)
            Text(text = "00:59", fontSize = 20.sp,textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),)
        }
    }
}

@Composable
fun DiffuseCode(diffuseCodeText: String, isCodeCorrect: Boolean, validCode: String){ //After settings set

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(5.dp, Color.LightGray),
        modifier = Modifier.fillMaxWidth()
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
                text = if (isCodeCorrect) "Correct" else "Incorrect",
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
        modifier = Modifier.fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 2.dp)
    ){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        ) {
            for (row in shuffledKeyboard){
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)
                ){
                    for(item in row){
                        Button(onClick = {
                            onItemClicked(item)
                            Log.d("Button", "Button clicked: $item")
                            shuffledKeyboard = shuffleKeyboard(keyboardArray)
                        }){
                            Text(text = item)
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