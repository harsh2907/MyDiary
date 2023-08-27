package com.example.mydiary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mydiary.presentation.screens.auth.AuthenticationScreen
import com.example.mydiary.presentation.screens.auth.AuthenticationViewModel
import com.example.mydiary.presentation.screens.home.HomeScreen
import com.example.mydiary.utils.Constants
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationScreen(){
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        }
        homeScreen(){
            navController.popBackStack()
            navController.navigate(Screen.Authentication.route)
        }
        writeScreen()
    }
}

fun NavGraphBuilder.authenticationScreen(
    navigateToHome: () -> Unit
) {
    composable(route = Screen.Authentication.route) {

        val authViewModel: AuthenticationViewModel = viewModel()
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val loadingState = authViewModel.loadingState.collectAsState().value
        val isAuthenticated = authViewModel.isAuthenticated.collectAsState().value

        AuthenticationScreen(
            isAuthenticated = isAuthenticated,
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            loadingState = loadingState,
            onButtonClick = {
                authViewModel.setLoading(true)
                oneTapSignInState.open()
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                authViewModel.setLoading(false)
            },
            onTokenIdReceived = { token ->
                authViewModel.signInWithMongoDB(
                    token = token,
                    onSuccess = { success ->
                        if (success) {
                            messageBarState.addSuccess("Successfully Authenticated")
                        }
                    },
                    onError = { error ->
                        messageBarState.addError(error)
                    }
                )
                authViewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeScreen(
    navigateToAuth: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val scope = rememberCoroutineScope()
            HomeScreen {
                scope.launch {
                    App.create(Constants.APP_ID).currentUser?.logOut()
                    navigateToAuth()
                }
            }
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