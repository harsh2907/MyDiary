package com.example.mydiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.mydiary.navigation.Screen
import com.example.mydiary.navigation.SetupNavGraph
import com.example.mydiary.ui.theme.MyDiaryTheme
import com.example.mydiary.utils.Constants
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MyDiaryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavGraph(
                        startDestination = getStartDestination(),
                        navController = rememberNavController()
                    )
                }
            }
        }
    }

    private fun getStartDestination():String{
        val user = App.create(Constants.APP_ID).currentUser

        return if(user!= null && user.loggedIn)
            Screen.Home.route
        else
            Screen.Authentication.route

    }
}
