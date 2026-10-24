package com.example.rickandmortyapp.screens.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.rickandmortyapp.viewmodels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    onCharacterClick: (Int) -> Unit,
    charactersViewModel: CharactersViewModel = viewModel()
) {
    val uiState by charactersViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            if (uiState.showFavoritesOnly) {
                                "Favorite characters"
                            } else {
                                "Characters"
                            }
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            charactersViewModel.toggleFavoritesFilter()
                        },
                        enabled = !uiState.isLoading
                    ) {
                        Icon(
                            imageVector =
                                if (uiState.showFavoritesOnly) {
                                    Icons.Default.Favorite
                                } else {
                                    Icons.Default.FavoriteBorder
                                },
                            contentDescription =
                                if (uiState.showFavoritesOnly) {
                                    "Mostrar todos"
                                } else {
                                    "Mostrar favoritos"
                                }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor =
                        MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.hasError -> {
                    ErrorScreen(
                        message = "Error al obtener listado de personajes.\nIntenta de nuevo",
                        onRetryClick = {
                            charactersViewModel.loadCharacters()
                        }
                    )
                }

                uiState.isLoading -> {
                    LoadingScreen(
                        onLoadingClick = {
                            charactersViewModel.showError()
                        }
                    )
                }

                uiState.showFavoritesOnly &&
                        uiState.data.isEmpty() -> {
                    EmptyFavoritesScreen()
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 10.dp
                        ),
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = uiState.data,
                            key = { character ->
                                character.id
                            }
                        ) { character ->
                            CharacterItem(
                                character = character,
                                onClick = {
                                    onCharacterClick(character.id)
                                },
                                onFavoriteClick = {
                                    charactersViewModel.toggleFavorite(
                                        character
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = "Imagen de ${character.name}",
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "${character.species} - ${character.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Gender: ${character.gender}",
                    style = MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onFavoriteClick
            ) {
                Icon(
                    imageVector =
                        if (character.isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                    contentDescription =
                        if (character.isFavorite) {
                            "Quitar de favoritos"
                        } else {
                            "Agregar a favoritos"
                        },
                    tint =
                        if (character.isFavorite) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                )
            }
        }
    }
}

@Composable
private fun EmptyFavoritesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay elementos para mostrar",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}