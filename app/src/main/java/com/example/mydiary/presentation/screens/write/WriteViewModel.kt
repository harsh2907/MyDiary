package com.example.mydiary.presentation.screens.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.repository.MongoDB
import com.example.mydiary.model.Diary
import com.example.mydiary.model.Mood
import com.example.mydiary.utils.Constants
import com.example.mydiary.utils.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getSelectedDiary()
    }

    fun upsertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ){
        viewModelScope.launch {
            if(uiState.value.selectedDiary!=null){
                updateDiary(diary, onSuccess, onError)
            }else{
                insertDiary(diary, onSuccess, onError)
            }
        }
    }

    private suspend fun insertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        val result = MongoDB.saveDiary(diary)
        when (result) {
            is RequestState.Error -> {
                withContext(Dispatchers.Main) {
                    onError(result.error.message)
                }
            }

            is RequestState.Success -> {
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }

            else -> Unit
        }
    }

    private suspend fun updateDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        val hexString = uiState.value.selectedDiaryId?.drop(13)?.dropLast(1) ?: return
        val result = MongoDB.updateDiary(diary.apply {
            _id = ObjectId(hexString)
            createdAt = uiState.value.selectedDiary!!.createdAt
        })
        when (result) {
            is RequestState.Error -> {
                withContext(Dispatchers.Main) {
                    onError(result.error.message)
                }
            }

            is RequestState.Success -> {
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }

            else -> Unit
        }
    }


    private fun getSelectedDiary() {
        viewModelScope.launch {
            savedStateHandle.get<String>(Constants.WRITE_SCREEN_ARGUMENT_KEY)?.let { diaryId ->
                val res = MongoDB.getDiaryById(diaryId).collect { res ->
                    if (res is RequestState.Success) {
                        _uiState.update {
                            it.copy(
                                selectedDiaryId = diaryId,
                                selectedDiary = res.data
                            )
                        }
                        setTitle(res.data?.title ?: "")
                        setDescription(res.data?.description ?: "")
                        setMood(res.data?.mood ?: Mood.Neutral.name)
                    }
                }

            }
        }
    }


    fun setTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun setDescription(description: String) {
        _uiState.update {
            it.copy(description = description)
        }
    }

    fun setMood(mood: String) {
        _uiState.update {
            it.copy(mood = mood)
        }
    }


    data class UiState(
        val selectedDiaryId: String? = null,
        val selectedDiary: Diary? = null,
        val title: String = "",
        val description: String = "",
        val mood: String = Mood.Neutral.name
    )
}