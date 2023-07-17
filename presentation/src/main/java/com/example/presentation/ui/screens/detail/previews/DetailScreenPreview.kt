package com.example.presentation.ui.screens.detail.previews

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.entity.Importance
import com.example.presentation.ui.screens.detail.components.Importance
import com.example.presentation.ui.screens.detail.components.AppBar
import com.example.presentation.ui.screens.detail.components.TodoDataPicker
import com.example.presentation.ui.screens.detail.components.ButtonDelete
import com.example.presentation.ui.screens.detail.components.MyDivider
import com.example.presentation.ui.screens.detail.components.InputText
import com.example.presentation.ui.theme.TodoAppCustomTheme
import com.example.presentation.ui.theme.TodoAppTheme
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState

@Preview
@Composable
private fun AppThemeCompose(){
    TodoAppTheme {
        val editTextValue = remember {
            mutableStateOf("")
        }
        val importantValue = remember {
            mutableStateOf(Importance.Basic)
        }
        val deadline = remember {
            mutableStateOf(System.currentTimeMillis())
        }
        val deadlineText = remember {
            mutableStateOf("00:00")
        }
        val dataStateVisible = remember {
            mutableStateOf(false)
        }
        val enableClick = remember {
            mutableStateOf(false)
        }

        var listState = rememberLazyListState()
        val topBarElevation by animateDpAsState(
            if(listState.canScrollBackward) 8.dp else 0.dp,
            label = "top bar elevation"
        )
        val clockState = rememberUseCaseState()
        Scaffold(
            topBar = { AppBar(true, topBarElevation, {}, {}) },
            containerColor = TodoAppCustomTheme.colors.backPrimary
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                state = listState
            ) {
                item {

                    InputText(editTextValue)
                    Importance(importantValue)
                    MyDivider(PaddingValues(horizontal = 16.dp))
                    TodoDataPicker(deadline,deadlineText, dataStateVisible)
                    MyDivider(PaddingValues(horizontal = 16.dp))
                    ButtonDelete(enableClick.value) {  }
                }
                item {
                    Spacer(modifier = Modifier.height(96.dp))
                }
            }
        }
    }
}