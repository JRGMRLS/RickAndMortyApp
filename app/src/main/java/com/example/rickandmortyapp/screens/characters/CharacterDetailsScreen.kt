package com.example.rickandmortyapp.screens.characters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.rickandmortyapp.Character
import com.example.rickandmortyapp.screens.components.ErrorScreen
import com.example.rickandmortyapp.screens.components.LoadingScreen
import com.example.rickandmortyapp.viewmodels.CharacterDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    onBackClick: () -> Unit,
    characterDetailsViewModel: CharacterDetailsViewModel = viewModel()
) {
    val uiState by characterDetailsViewModel.uiState.collectAsState()
    val character = uiState.data

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Character details")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            characterDetailsViewModel.toggleFavorite()
                        },
                        enabled = character != null
                    ) {
                        Icon(
                            imageVector =
                                if (character?.isFavorite == true) {
                                    Icons.Default.Favorite
                                } else {
                                    Icons.Default.FavoriteBorder
                                },
                            contentDescription =
                                if (character?.isFavorite == true) {
                                    "Quitar de favoritos"
                                } else {
                                    "Agregar a favoritos"
                                }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor =
                        MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor =
                        MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.hasError -> {
                    ErrorScreen(
                        message = "Error al obtener el personaje.\nIntenta de nuevo",
                        onRetryClick = {
                            characterDetailsViewModel.loadCharacter()
                        }
                    )
                }

                uiState.isLoading -> {
                    LoadingScreen(
                        onLoadingClick = {
                            characterDetailsViewModel.showError()
                        }
                    )
                }

                character != null -> {
                    CharacterDetailsContent(
                        character = character
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailsContent(
    character: Character
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = "Imagen de ${character.name}",
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(22.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        CharacterInformationRow(
            label = "Species:",
            value = character.species
        )

        CharacterInformationRow(
            label = "Status:",
            value = character.status
        )

        CharacterInformationRow(
            label = "Gender:",
            value = character.gender
        )

        CharacterInformationRow(
            label = "Favorite:",
            value =
                if (character.isFavorite) {
                    "Yes"
                } else {
                    "No"
                }
        )
    }
}

@Composable
private fun CharacterInformationRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}