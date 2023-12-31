package com.example.mydiary.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mydiary.R
import com.example.mydiary.presentation.components.GoogleSignInButton

@Composable
fun AuthScreenContent(
    modifier: Modifier = Modifier,
    loadingState:Boolean,
    onButtonClicked:()->Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = "Your Daily Journal!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "Sign in now and embark on an interactive journey of self-expression!",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.padding(10.dp))

        GoogleSignInButton(
            text = "Sign in with google",
            iconPainter = painterResource(id = R.drawable.google_logo),
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            isLoading = loadingState,
            onClick = onButtonClicked
        )
    }
}

