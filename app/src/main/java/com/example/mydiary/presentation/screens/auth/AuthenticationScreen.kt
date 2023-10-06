package com.example.mydiary.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.mydiary.utils.Constants
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle


@Composable
fun AuthenticationScreen(
    isAuthenticated: Boolean,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    loadingState: Boolean,
    onButtonClick: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateToHome: () -> Unit
) {

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding()

    ) { padding ->
        ContentWithMessageBar(messageBarState = messageBarState) {
            AuthScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                loadingState = loadingState,
                onButtonClicked = onButtonClick
            )
        }
    }

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = Constants.CLIENT_ID,
        onTokenIdReceived = onTokenIdReceived,
        onDialogDismissed = onDialogDismissed
    )

    LaunchedEffect(key1 = isAuthenticated) {
        if (isAuthenticated) {
            navigateToHome()
        }
    }
}