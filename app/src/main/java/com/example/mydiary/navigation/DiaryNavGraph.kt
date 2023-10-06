package com.example.mydiary.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mydiary.data.repository.MongoDB
import com.example.mydiary.model.Mood
import com.example.mydiary.presentation.components.DisplayAlertDialog
import com.example.mydiary.presentation.screens.auth.AuthenticationScreen
import com.example.mydiary.presentation.screens.auth.AuthenticationViewModel
import com.example.mydiary.presentation.screens.home.HomeScreen
import com.example.mydiary.presentation.screens.home.HomeViewModel
import com.example.mydiary.presentation.screens.write.WriteScreen
import com.example.mydiary.presentation.screens.write.WriteViewModel
import com.example.mydiary.utils.Constants
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationScreen {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        }
        homeScreen(
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            },
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToWriteWithArgs = { dairyId ->
                navController.navigate(Screen.Write.passDairyId(dairyId))
            }
        )
        writeScreen(
            onBackPressed = {
                navController.popBackStack()
            }
        )
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
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated")
                        authViewModel.setLoading(false)
                    },
                    onError = { error ->
                        messageBarState.addError(error)
                        authViewModel.setLoading(false)

                    }
                )
            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeScreen(
    navigateToAuth: () -> Unit,
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit
) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val viewModel: HomeViewModel = viewModel()
        val diaries = viewModel.diaries.collectAsStateWithLifecycle().value

        val scope = rememberCoroutineScope()
        var signOutDialogOpened by remember { mutableStateOf(false) }

        HomeScreen(
            diaries = diaries,
            drawerState = drawerState,
            navigateToWrite = navigateToWrite,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onDiaryClicked = navigateToWriteWithArgs,
            onSignOutClicked = {
                signOutDialogOpened = true
            }
        )

        LaunchedEffect(key1 = Unit) {
            MongoDB.configureTheRealm()
        }

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to Sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            onCloseDialog = {
                signOutDialogOpened = false
            },
            onConfirmClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(Constants.APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeScreen(
    onBackPressed: () -> Unit
) {
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

        val viewModel: WriteViewModel = viewModel()
        val pagerState = rememberPagerState()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
        val messageBarState = rememberMessageBarState()
        var loadingState by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        WriteScreen(
            loadingState = loadingState,
            messageBarState = messageBarState,
            uiState = uiState,
            pagerState = pagerState,
            selectedDiary = uiState.selectedDiary,
            onDeleteClicked = { },
            onBackPressed = onBackPressed,
            onTitleChange = viewModel::setTitle,
            onDescriptionChange = viewModel::setDescription,
            moodName = { Mood.values()[pageNumber].name },
            onSave = { diary ->
                loadingState = true
                viewModel.upsertDiary(
                    diary = diary.apply { this.mood = Mood.values()[pageNumber].name },
                    onSuccess = {
                        scope.launch(Dispatchers.Main) {
                            messageBarState.addSuccess("Diary Saved Successfully!")
                            delay(600)
                            loadingState = false
                            onBackPressed()
                        }
                    },
                    onError = { error ->
                        loadingState = false
                        messageBarState.addError(
                            Exception(error)
                        )
                    }
                )
            }
        )
    }
}