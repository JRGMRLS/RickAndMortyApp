package com.example.rickandmortyapp.screens.session

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmortyapp.viewmodels.SessionViewModel

@Composable
fun SessionScreen(
    onLoggedIn: () -> Unit,
    onNotLoggedIn: () -> Unit,
    sessionViewModel: SessionViewModel = viewModel()
) {
    val uiState by sessionViewModel.uiState.collectAsState()

    LaunchedEffect(
        uiState.isLoading,
        uiState.isLoggedIn
    ) {
        if (!uiState.isLoading) {
            if (uiState.isLoggedIn) {
                onLoggedIn()
            } else {
                onNotLoggedIn()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
    }
}