package com.example.mydiary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mydiary.utils.Constants

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationScreen()
        homeScreen()
        writeScreen()
    }
}

fun NavGraphBuilder.authenticationScreen() {
    composable(route = Screen.Authentication.route) {

    }
}

fun NavGraphBuilder.homeScreen() {
    composable(route = Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeScreen() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(
            name = Constants.WRITE_SCREEN_ARGUMENT_KEY
        ) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}