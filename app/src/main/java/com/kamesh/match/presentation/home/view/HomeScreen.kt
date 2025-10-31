package com.kamesh.match.presentation.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.kamesh.match.presentation.home.components.HomeProfileCard
import com.kamesh.match.presentation.home.viewmodel.HomeViewModel
import com.kamesh.match.presentation.navigation.Screen
import com.kamesh.match.presentation.profile.model.ProfileEvent

/**
 * Home Screen - Main Matches Display
 * 
 * Shows profiles in horizontal scrollable cards where users can:
 * - Browse through potential matches
 * - Like profiles (heart button) - shows personalized snackbar
 * - Dislike profiles (X button) - shows friendly snackbar  
 * - Tap cards to view detailed profile information
 * - Navigate to daily matches screen via menu
 * 
 * Features:
 * - Horizontal card layout with smooth scrolling
 * - Live profile count with "NEW" premium indicator
 * - Snackbar feedback for user actions
 * - Loading state management
 * - Blue theme with modern Material 3 design
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    // Collect UI state from ViewModel
    val state by viewModel.state.collectAsState()
    // Setup snackbar for user feedback
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        // Add snackbar support for like/dislike feedback
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // Top app bar with navigation to daily matches
        topBar = {
            TopAppBar(
                title = { Text(text = "My Matches", color = Color.White) },
                actions = {
                    // Menu button to navigate to Profile/Daily screen
                    IconButton(onClick = { navController.navigate(Screen.ProfileScreen.route) }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00A3E0) // Blue theme
                )
            )
        },
        containerColor = Color(0xFF00A3E0) // Blue background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Profile count header with premium indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dynamic profile count display
                Text(
                    text = "${state.profiles.size} Profiles pending with me",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Premium NRI indicator badge
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${state.profiles.count { it.isPremiumNri }} NEW",
                        color = Color(0xFF00A3E0),
                        fontSize = 10.sp
                    )
                }
            }

            // Main content area - loading or profile cards
            if (state.isLoading) {
                // Show loading spinner while data loads
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Horizontal scrollable list of profile cards
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(
                        items = state.profiles,
                        key = { profile -> profile.id } // Stable keys for better performance
                    ) { profile ->
                        HomeProfileCard(
                            profile = profile,
                            modifier = Modifier.width(300.dp), // Fixed card width
                            
                            // Like action - show personalized success message
                            onLike = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("💖 Great choice! ${profile.name} has been liked")
                                }
                                viewModel.onEvent(ProfileEvent.LikeProfile(profile))
                            },
                            
                            // Dislike action - show friendly message
                            onDislike = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("👋 No worries! You passed on ${profile.name}")
                                }
                                viewModel.onEvent(ProfileEvent.DislikeProfile(profile))
                            },
                            
                            // Card tap - navigate to detailed view
                            onClick = {
                                viewModel.onEvent(ProfileEvent.SelectProfile(profile))
                                navController.navigate(Screen.ProfileDetailsScreen.route)
                            }
                        )
                    }
                }
            }
        }
    }
}