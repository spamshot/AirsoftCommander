package com.example.airsoftcommander.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


data class Games(
    val id: Int,
    @StringRes val title: Int,
    @DrawableRes val imageResourceId: Int
)