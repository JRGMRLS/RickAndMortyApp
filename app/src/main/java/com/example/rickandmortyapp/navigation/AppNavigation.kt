package com.example.rickandmortyapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rickandmortyapp.screens.home.HomeScreen
import com.example.rickandmortyapp.screens.login.LoginScreen
import com.example.rickandmortyapp.screens.session.SessionScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SessionDestination
    ) {
        composable<SessionDestination> {
            SessionScreen(
                onLoggedIn = {
                    navController.navigate(HomeDestination) {
                        popUpTo<SessionDestination> {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onNotLoggedIn = {
                    navController.navigate(LoginDestination) {
                        popUpTo<SessionDestination> {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        composable<LoginDestination> {
            LoginScreen(
                onStartClick = {
                    navController.navigate(HomeDestination) {
                        popUpTo<LoginDestination> {
                            inclusive = true
                        }

                        launchSingleTop = true
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