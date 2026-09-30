package com.example.rickandmortyapp.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rickandmortyapp.CharacterDb
import com.example.rickandmortyapp.LocationDb
import com.example.rickandmortyapp.navigation.CharacterDetailsDestination
import com.example.rickandmortyapp.navigation.CharactersDestination
import com.example.rickandmortyapp.navigation.CharactersGraph
import com.example.rickandmortyapp.navigation.LocationDetailsDestination
import com.example.rickandmortyapp.navigation.LocationsDestination
import com.example.rickandmortyapp.navigation.LocationsGraph
import com.example.rickandmortyapp.navigation.ProfileDestination
import com.example.rickandmortyapp.navigation.charactersNavigation
import com.example.rickandmortyapp.navigation.locationsNavigation
import com.example.rickandmortyapp.screens.profile.ProfileScreen

@Composable
fun HomeScreen(
    onLogoutClick: () -> Unit
) {
    val navController = rememberNavController()
    val characterDb = remember { CharacterDb() }
    val locationDb = remember { LocationDb() }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val charactersSelected =
        currentDestination?.hasRoute<CharactersDestination>() == true ||
                currentDestination?.hasRoute<CharacterDetailsDestination>() == true

    val locationsSelected =
        currentDestination?.hasRoute<LocationsDestination>() == true ||
                currentDestination?.hasRoute<LocationDetailsDestination>() == true

    val profileSelected =
        currentDestination?.hasRoute<ProfileDestination>() == true

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = charactersSelected,
                    onClick = {
                        navController.navigate(CharactersGraph) {
                            popUpTo(
                                navController.graph
                                    .findStartDestination()
                                    .id
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Characters"
                        )
                    },
                    label = {
                        Text(text = "Characters")
                    }
                )

                NavigationBarItem(
                    selected = locationsSelected,
                    onClick = {
                        navController.navigate(LocationsGraph) {
                            popUpTo(
                                navController.graph
                                    .findStartDestination()
                                    .id
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Locations"
                        )
                    },
                    label = {
                        Text(text = "Locations")
                    }
                )

                NavigationBarItem(
                    selected = profileSelected,
                    onClick = {
                        navController.navigate(ProfileDestination) {
                            popUpTo(
                                navController.graph
                                    .findStartDestination()
                                    .id
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        Text(text = "Profile")
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            charactersNavigation(
                navController = navController,
                characterDb = characterDb
            )

            locationsNavigation(
                navController = navController,
                locationDb = locationDb
            )

            composable<ProfileDestination> {
                ProfileScreen(
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}