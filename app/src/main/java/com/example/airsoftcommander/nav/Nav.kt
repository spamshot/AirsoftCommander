package com.example.airsoftcommander.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.airsoftcommander.data.GamesListDataSource
import com.example.airsoftcommander.screens.GamesList
import androidx.navigation.NavController
import com.example.airsoftcommander.screens.TacticalBombScreen

@Composable
fun Nav(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start_screen"){
        composable("start_screen"){ StartScreen(navController) }
        composable("bomb_screen"){TacticalBombScreen(navController)}
    }

}
@Composable
fun StartScreen(navController: NavController){
    val gamesList = GamesListDataSource().loadGames()
    GamesList(gamesList, navController)
}