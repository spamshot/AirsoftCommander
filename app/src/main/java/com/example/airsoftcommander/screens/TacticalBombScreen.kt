package com.example.airsoftcommander.screens

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

//todo random timer bomb game


@Composable
fun TacticalBombScreen(navController: NavController){

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
                    modifier = Modifier
                        .weight(1f))

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
                    modifier = Modifier.weight(1f))

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
                    modifier = Modifier.weight(1f))

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
                    modifier = Modifier.weight(1f))

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
fun BombGame(){
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()) {
        Text(text = "Tactical Bomb Started", textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge)
    }

}


@Preview
@Composable
fun myPreview(){
    BombGame()
}
