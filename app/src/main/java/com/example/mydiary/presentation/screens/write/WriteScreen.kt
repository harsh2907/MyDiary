package com.example.mydiary.presentation.screens.write

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mydiary.model.Diary
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import java.time.ZonedDateTime

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WriteScreen(
    uiState: WriteViewModel.UiState,
    pagerState: PagerState,
    messageBarState: MessageBarState,
    loadingState: Boolean,
    selectedDiary: Diary?,
    onDeleteClicked: () -> Unit,
    onBackPressed: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    moodName: () -> String,
    onSave: (Diary) -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit
) {

    ContentWithMessageBar(
        messageBarState = messageBarState,
        errorMaxLines = 5,
        modifier = Modifier.statusBarsPadding()
    ) {
        Scaffold(
            topBar = {
                WriteScreenTopBar(
                    moodName = moodName,
                    selectedDiary = selectedDiary,
                    onDeleteClicked = onDeleteClicked,
                    onBackPressed = onBackPressed,
                    onDateTimeUpdated = onDateTimeUpdated
                )
            }
        ) { paddingValues ->
            WriteScreenContent(
                uiState = uiState,
                loadingState = loadingState,
                messageBarState = messageBarState,
                paddingValues = paddingValues,
                pagerState = pagerState,
                title = uiState.title,
                onTitleChange = onTitleChange,
                description = uiState.description,
                onDescriptionChange = onDescriptionChange,
                mood = uiState.mood,
                onSave = onSave
            )
        }
    }
}