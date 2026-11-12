package com.example.rickandmortyapp.screens.locations

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmortyapp.Location
import com.example.rickandmortyapp.screens.components.ErrorScreen
import com.example.rickandmortyapp.screens.components.LoadingScreen
import com.example.rickandmortyapp.viewmodels.LocationFilter
import com.example.rickandmortyapp.viewmodels.LocationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    onLocationClick: (Int) -> Unit,
    locationsViewModel: LocationsViewModel = viewModel()
) {
    val uiState by locationsViewModel.uiState.collectAsState()

    var showFilterMenu by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            when (uiState.filter) {
                                LocationFilter.ALL ->
                                    "Locations"

                                LocationFilter.FAVORITES ->
                                    "Favorite locations"

                                LocationFilter.NON_FAVORITES ->
                                    "Non-favorite locations"
                            }
                    )
                },
                actions = {
                    Box {
                        IconButton(
                            onClick = {
                                showFilterMenu = true
                            },
                            enabled = !uiState.isLoading
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Default.FilterList,
                                contentDescription =
                                    "Filtrar locations"
                            )
                        }

                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = {
                                showFilterMenu = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = "Todas")
                                },
                                onClick = {
                                    showFilterMenu = false
                                    locationsViewModel.setFilter(
                                        LocationFilter.ALL
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(text = "Favoritas")
                                },
                                onClick = {
                                    showFilterMenu = false
                                    locationsViewModel.setFilter(
                                        LocationFilter.FAVORITES
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(text = "No favoritas")
                                },
                                onClick = {
                                    showFilterMenu = false
                                    locationsViewModel.setFilter(
                                        LocationFilter.NON_FAVORITES
                                    )
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,
                    titleContentColor =
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
                        message =
                            "Error al obtener listado de ubicaciones.\n" +
                                    "Intenta de nuevo",
                        onRetryClick = {
                            locationsViewModel.loadLocations()
                        }
                    )
                }

                uiState.isLoading -> {
                    LoadingScreen(
                        onLoadingClick = {
                            locationsViewModel.showError()
                        }
                    )
                }

                uiState.data.isEmpty() -> {
                    EmptyLocationsScreen()
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
                            key = { location ->
                                location.id
                            }
                        ) { location ->
                            LocationItem(
                                location = location,
                                onClick = {
                                    onLocationClick(location.id)
                                },
                                onFavoriteClick = {
                                    locationsViewModel
                                        .toggleFavorite(location)
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
private fun LocationItem(
    location: Location,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = location.type,
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onFavoriteClick
            ) {
                Icon(
                    imageVector =
                        if (location.isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                    contentDescription =
                        if (location.isFavorite) {
                            "Quitar de favoritas"
                        } else {
                            "Agregar a favoritas"
                        },
                    tint =
                        if (location.isFavorite) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                        }
                )
            }
        }
    }
}

@Composable
private fun EmptyLocationsScreen() {
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