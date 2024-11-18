package com.example.airsoftcommander.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

//todo random timer bomb game


@Composable
fun TacticalBombScreen(navController: NavController){
    Column (modifier = Modifier.fillMaxSize()){
        Text(text = "Tactical Bomb Screen")
    }
}