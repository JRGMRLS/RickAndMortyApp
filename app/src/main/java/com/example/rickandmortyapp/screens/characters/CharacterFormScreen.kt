package com.example.rickandmortyapp.screens.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmortyapp.viewmodels.CharacterFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    characterFormViewModel: CharacterFormViewModel = viewModel()
) {
    val uiState by characterFormViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            if (uiState.isEditing) {
                                "Edit character"
                            } else {
                                "New character"
                            }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,
                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor =
                        MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange =
                        characterFormViewModel::onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Name *")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.status,
                    onValueChange =
                        characterFormViewModel::onStatusChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Status *")
                    },
                    placeholder = {
                        Text(text = "Alive, Dead or unknown")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.species,
                    onValueChange =
                        characterFormViewModel::onSpeciesChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Species *")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.gender,
                    onValueChange =
                        characterFormViewModel::onGenderChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Gender *")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.image,
                    onValueChange =
                        characterFormViewModel::onImageChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Image URL")
                    },
                    minLines = 2
                )

                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        characterFormViewModel.saveCharacter(
                            onSuccess = onSaved
                        )
                    },
                    enabled = !uiState.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            color =
                                MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text =
                                if (uiState.isEditing) {
                                    "Save changes"
                                } else {
                                    "Create character"
                                }
                        )
                    }
                }
            }
        }
    }
}