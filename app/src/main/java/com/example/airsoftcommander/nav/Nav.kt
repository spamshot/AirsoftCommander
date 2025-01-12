package com.example.airsoftcommander.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.airsoftcommander.data.GamesListDataSource
import com.example.airsoftcommander.screens.GamesList
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.airsoftcommander.screens.TacticalBombEndScreen
import com.example.airsoftcommander.screens.TacticalBombScreen

@Composable
fun Nav(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start_screen"){
        composable("start_screen"){ StartScreen(navController) }
        composable("bomb_screen"){TacticalBombScreen(navController)}
        composable("end_screen/{isBombDefused}",
            arguments = listOf(navArgument("isBombDefused"){type = NavType.BoolType})
        ){ backStackEntry ->
            val isBombDefused = backStackEntry.arguments?.getBoolean("isBombDefused") ?: false
            TacticalBombEndScreen(navController, isBombDefused)
        }
            //TacticalBombEndScreen(navController)}
    }

}
@Composable
fun StartScreen(navController: NavController){
    val gamesList = GamesListDataSource().loadGames()
    GamesList(gamesList, navController)
}