package com.kamesh.match.presentation.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object ProfileScreen : Screen("profile_screen")
    object ProfileDetailsScreen : Screen("profile_details_screen")
}