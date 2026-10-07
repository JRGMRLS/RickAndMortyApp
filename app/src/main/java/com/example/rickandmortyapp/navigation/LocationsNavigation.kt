package com.example.rickandmortyapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.rickandmortyapp.screens.locations.LocationDetailsScreen
import com.example.rickandmortyapp.screens.locations.LocationsScreen

fun NavGraphBuilder.locationsNavigation(
    navController: NavHostController
) {
    navigation<LocationsGraph>(
        startDestination = LocationsDestination
    ) {
        composable<LocationsDestination> {
            LocationsScreen(
                onLocationClick = { locationId ->
                    navController.navigate(
                        LocationDetailsDestination(
                            id = locationId
                        )
                    )
                }
            )
        }

        composable<LocationDetailsDestination> {
            LocationDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}