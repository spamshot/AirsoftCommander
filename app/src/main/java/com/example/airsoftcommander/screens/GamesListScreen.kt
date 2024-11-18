package com.example.airsoftcommander.screens

import com.example.airsoftcommander.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.navigation.NavController
import com.example.airsoftcommander.model.Games


//Lazy column with all the games

@Composable
fun GamesList(gamesList: List<Games>, navController: NavController) {
    LazyColumn (modifier = Modifier.fillMaxSize().padding(top = 40.dp)){
        items(gamesList) { games ->
            val route = when (games.title){
                R.string.bomb -> "bomb_screen"
                R.string.gun -> "gun"
                else -> "error"
            }
            GamesCard(games, navController, route)
        }
    }
}


@Composable
fun GamesCard(games: Games, navController: NavController, route: String) {
    Card (modifier = Modifier.fillMaxWidth()
        .padding(8.dp)
        .clickable {
            navController.navigate(route)

        }){
        Column {
            Image(
                painter = painterResource(games.imageResourceId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Text(
                text = stringResource(games.title),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 2.dp, bottom = 2.dp)
                    .fillMaxWidth(),
                color = colorResource(id = R.color.black)
            )
        }
    }
}