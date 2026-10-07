package com.example.rickandmortyapp.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.rickandmortyapp.screens.characters.CharacterDetailsScreen
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
                }
            )
        }

        composable<CharacterDetailsDestination> {
            CharacterDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}