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
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(Constants.APP_ID).login(
                        Credentials.jwt(token)
                    ).loggedIn
                }

                withContext(Dispatchers.IO) {
                    if(result){
                        onSuccess()
                        delay(600)
                        isAuthenticated.value = true
                    }
                    else
                        onError(Exception("User not logged in."))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    onError(e)
                }
            }
        }
    }
}