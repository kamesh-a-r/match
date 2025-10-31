package com.kamesh.match.presentation.navigation
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kamesh.match.presentation.details.view.ProfileDetailsScreen
import com.kamesh.match.presentation.home.view.HomeScreen
import com.kamesh.match.presentation.home.viewmodel.HomeViewModel
import com.kamesh.match.presentation.profile.view.ProfileScreen
import com.kamesh.match.presentation.profile.viewmodel.ProfileViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    
    // Create ViewModels at NavGraph level so they can be shared
    val homeViewModel: HomeViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel
            )
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController, viewModel = profileViewModel)
        }
        composable(Screen.ProfileDetailsScreen.route) {
            // Determine which ViewModel to use based on selectedProfile
            // If homeViewModel has selectedProfile, use it; otherwise use profileViewModel
            val viewModelToUse = if (homeViewModel.state.collectAsState().value.selectedProfile != null) {
                homeViewModel
            } else {
                profileViewModel
            }
            ProfileDetailsScreen(navController = navController, viewModel = viewModelToUse)
        }
    }
}