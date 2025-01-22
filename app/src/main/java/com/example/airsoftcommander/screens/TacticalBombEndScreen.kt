package com.example.airsoftcommander.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.airsoftcommander.R

//class TacticalBombEndScreen : ViewModel(){
//
//}

@Composable
fun TacticalBombEndScreen(
    navController: NavController,isBombDefused: Boolean){
    Column (modifier = Modifier.fillMaxSize()){
        if (isBombDefused) {
            BombDiffusedScreen()
            EndGameButton(navController)
        }else{
            BombDetonatedScreen()
            EndGameButton(navController)
        }
    }


}



@Composable
fun BombDiffusedScreen(){
    Card(modifier = Modifier.fillMaxWidth()
        .padding(start = 10.dp, top = 55.dp, end = 10.dp)
    ){
        Column {
            Image(
                painter = painterResource(R.drawable.goodjobimg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = "Bomb Diffused",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 2.dp, bottom = 2.dp)
                    .fillMaxWidth(),
                color = colorResource(id = R.color.black)
            )
        }

    }

}

@Composable
fun BombDetonatedScreen(){
    Card(modifier = Modifier.fillMaxWidth()
        .padding(start = 10.dp, top = 55.dp, end = 10.dp),
    ){
        Column (){
            Image(
                painter = painterResource(R.drawable.failedimg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = "Bomb Detonated!",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 2.dp, bottom = 2.dp)
                    .fillMaxWidth(),
                color = colorResource(id = R.color.black)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TacticalBombEndScreenPreview() {
    BombDiffusedScreen()
}

@Composable
fun EndGameButton(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 42.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(onClick = {
            navController.navigate("start_screen")
        },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Home", fontSize = 30.sp )
        }

    }
}