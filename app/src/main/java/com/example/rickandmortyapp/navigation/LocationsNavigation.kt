package com.example.rickandmortyapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.rickandmortyapp.LocationDb
import com.example.rickandmortyapp.screens.locations.LocationDetailsScreen
import com.example.rickandmortyapp.screens.locations.LocationsScreen

fun NavGraphBuilder.locationsNavigation(
    navController: NavHostController,
    locationDb: LocationDb
) {
    navigation<LocationsGraph>(
        startDestination = LocationsDestination
    ) {
        composable<LocationsDestination> {
            LocationsScreen(
                locations = locationDb.getAllLocations(),
                onLocationClick = { locationId ->
                    navController.navigate(
                        LocationDetailsDestination(
                            id = locationId
                        )
                    )
                }
            )
        }

        composable<LocationDetailsDestination> { backStackEntry ->
            val destination =
                backStackEntry.toRoute<LocationDetailsDestination>()

            val location =
                locationDb.getLocationById(destination.id)

            LocationDetailsScreen(
                location = location,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}