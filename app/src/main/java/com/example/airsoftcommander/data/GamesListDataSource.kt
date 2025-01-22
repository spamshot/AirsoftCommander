package com.example.airsoftcommander.data
import com.example.airsoftcommander.R
import com.example.airsoftcommander.model.Games

class GamesListDataSource {

    fun loadGames(): List<Games> {
        return listOf(
            Games(1, R.string.bomb, R.drawable.timericon),
            //Games(2, R.string.gun, R.drawable.gunimg)
        )
    }
}
//todo images from Copilot, Any games get added to this list

