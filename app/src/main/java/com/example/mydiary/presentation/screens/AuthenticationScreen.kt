package com.example.mydiary.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AuthenticationScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ){padding->
        AuthScreenContent(
            modifier = Modifier.padding(padding)
        )
    }
}