package com.example.mydiary.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.mydiary.model.Diary
import com.example.mydiary.presentation.components.DisplayAlertDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteScreenTopBar(
    moodName:()->String,
    selectedDiary: Diary?,
    onDeleteClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = moodName(),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "10 JAN 2023, 10:00 AM",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "calendar",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            if (selectedDiary != null) {
                DeleteDiaryAction(
                    selectedDiary = selectedDiary,
                    onDeleteClicked = onDeleteClicked
                )
            }
        }
    )
}


@Composable
fun DeleteDiaryAction(
    selectedDiary: Diary,
    onDeleteClicked: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        }) {
        DropdownMenuItem(
            text = { Text(text = "Delete") },
            onClick = { showDialog = true }
        )
    }

    DisplayAlertDialog(
        title = "Delete",
        message = "Do you want to permanently delete this diary note '${selectedDiary.title}' ",
        dialogOpened = showDialog,
        onCloseDialog = { showDialog = false },
        onConfirmClicked = onDeleteClicked
    )

    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "more options")
    }


}