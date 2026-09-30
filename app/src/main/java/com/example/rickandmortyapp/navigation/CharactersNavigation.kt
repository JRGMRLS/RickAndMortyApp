package com.example.rickandmortyapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.rickandmortyapp.CharacterDb
import com.example.rickandmortyapp.screens.characters.CharacterDetailsScreen
import com.example.rickandmortyapp.screens.characters.CharactersScreen

fun NavGraphBuilder.charactersNavigation(
    navController: NavHostController,
    characterDb: CharacterDb
) {
    navigation<CharactersGraph>(
        startDestination = CharactersDestination
    ) {
        composable<CharactersDestination> {
            CharactersScreen(
                characters = characterDb.getAllCharacters(),
                onCharacterClick = { characterId ->
                    navController.navigate(
                        CharacterDetailsDestination(
                            id = characterId
                        )
                    )
                }
            )
        }

        composable<CharacterDetailsDestination> { backStackEntry ->
            val destination =
                backStackEntry.toRoute<CharacterDetailsDestination>()

            val character =
                characterDb.getCharacterById(destination.id)

            CharacterDetailsScreen(
                character = character,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}