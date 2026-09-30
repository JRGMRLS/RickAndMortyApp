package com.example.rickandmortyapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rickandmortyapp.screens.home.HomeScreen
import com.example.rickandmortyapp.screens.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {
        composable<LoginDestination> {
            LoginScreen(
                onStartClick = {
                    navController.navigate(HomeDestination) {
                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<HomeDestination> {
            HomeScreen(
                onLogoutClick = {
                    navController.navigate(LoginDestination) {
                        popUpTo<HomeDestination> {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }
    }
}