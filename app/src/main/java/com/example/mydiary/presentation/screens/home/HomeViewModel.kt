package com.example.mydiary.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.repository.Diaries
import com.example.mydiary.data.repository.MongoDB
import com.example.mydiary.utils.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(){

    private val _diaries:MutableStateFlow<Diaries> = MutableStateFlow(RequestState.Loading)
    val diaries = _diaries.asStateFlow()

    init {
        getAllDiaries()
    }

    fun getAllDiaries(){
        viewModelScope.launch {
            MongoDB.getAllDiaries().collectLatest {res->
                _diaries.value = res
            }
        }
    }
}