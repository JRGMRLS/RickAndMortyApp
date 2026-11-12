package com.example.rickandmortyapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.rickandmortyapp.screens.characters.CharacterDetailsScreen
import com.example.rickandmortyapp.screens.characters.CharacterFormScreen
import com.example.rickandmortyapp.screens.characters.CharactersScreen

fun NavGraphBuilder.charactersNavigation(
    navController: NavHostController
) {
    navigation<CharactersGraph>(
        startDestination = CharactersDestination
    ) {
        composable<CharactersDestination> {
            CharactersScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(
                        CharacterDetailsDestination(
                            id = characterId
                        )
                    )
                },
                onAddCharacterClick = {
                    navController.navigate(
                        CharacterFormDestination()
                    )
                }
            )
        }

        composable<CharacterDetailsDestination> { backStackEntry ->
            val destination =
                backStackEntry
                    .toRoute<CharacterDetailsDestination>()

            CharacterDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = {
                    navController.navigate(
                        CharacterFormDestination(
                            id = destination.id
                        )
                    )
                },
                onDeleted = {
                    navController.popBackStack()
                }
            )
        }

        composable<CharacterFormDestination> {
            CharacterFormScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaved = {
                    navController.popBackStack()
                }
            )
        }
    }
}