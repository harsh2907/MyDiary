package com.example.mydiary.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.utils.Constants
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {

    val loadingState = MutableStateFlow(false)
    val isAuthenticated = MutableStateFlow(false)

    fun setLoading(loading: Boolean) {
        loadingState.value = loading
    }

    fun signInWithMongoDB(
        token: String,
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(Constants.APP_ID).login(
                        Credentials.jwt(token)
                     //   Credentials.google(token, GoogleAuthType.ID_TOKEN)
                    ).loggedIn
                }

                withContext(Dispatchers.IO) {
                    onSuccess(result)
                    delay(600)
                    isAuthenticated.value = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    onError(e)
                }
            }
        }
    }
}