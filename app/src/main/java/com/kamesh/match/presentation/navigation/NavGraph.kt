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

/**
 * Composable function that defines the application's navigation graph.
 *
 * It sets up a [NavHost] with all the possible navigation destinations (screens)
 * and manages the navigation controller. This component also instantiates and scopes
 * ViewModels like [HomeViewModel] and [ProfileViewModel] to the navigation graph,
 * allowing them to be shared across multiple composables within the graph.
 *
 * The graph includes the following screens:
 * - [Screen.HomeScreen]: The starting destination of the app.
 * - [Screen.ProfileScreen]: A screen to display a user's own profile.
 * - [Screen.ProfileDetailsScreen]: A screen to display details of a selected profile.
 *   This screen intelligently uses either the [HomeViewModel] or the [ProfileViewModel]
 *   based on the navigation origin to display the correct profile data.
 */
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