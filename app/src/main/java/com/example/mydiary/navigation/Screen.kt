package com.example.mydiary.navigation

import com.example.mydiary.utils.Constants

sealed class Screen(val route:String) {
     data object Authentication:Screen("authentication_screen")
     data object Home:Screen("home_screen")
     data object Write:Screen("write_screen?${Constants.WRITE_SCREEN_ARGUMENT_KEY}={${Constants.WRITE_SCREEN_ARGUMENT_KEY}}"){
         fun passDairyId(diaryId:String) = "write_screen?${Constants.WRITE_SCREEN_ARGUMENT_KEY}=$diaryId"
     }
}