package com.example.airsoftcommander.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.airsoftcommander.R

//class TacticalBombEndScreen : ViewModel(){
//
//}

@Composable
fun TacticalBombEndScreen(
    navController: NavController,isBombDefused: Boolean){
    if (isBombDefused) {
        BombDiffusedScreen()
    }else{
        BombDetonatedScreen()
    }


}



@Composable
fun BombDiffusedScreen(){
    Card(modifier = Modifier.fillMaxWidth()
        .padding(start = 10.dp, top = 40.dp, end = 10.dp)
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
        .padding(start = 10.dp, top = 40.dp, end = 10.dp)
    ){
        Column {
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