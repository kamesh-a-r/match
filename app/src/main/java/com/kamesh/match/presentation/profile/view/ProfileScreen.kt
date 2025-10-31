package com.kamesh.match.presentation.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.kamesh.match.presentation.widget.cardStack.model.DragValue
import com.kamesh.match.presentation.widget.cardStack.view.TopStackedCardView
import com.kamesh.match.presentation.navigation.Screen
import com.kamesh.match.presentation.profile.viewmodel.ProfileViewModel
import com.kamesh.match.presentation.profile.model.ProfileEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    val state by viewModel.state.collectAsState()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Daily Recommendations") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                modifier = Modifier.background(Color.Transparent),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF20BF55), // green
                            Color(0xFF2A52BE)  // blue
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight(0.5f)
                    .background(Color(0xFFF5F5F5))
            )

            Box(modifier = Modifier.padding(paddingValues)) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    state.profiles.isNotEmpty() -> {
                        val visibleProfiles = state.profiles.subList(state.currentCardIndex, state.profiles.size)
                        if (visibleProfiles.isNotEmpty()) {
                            TopStackedCardView(
                                profiles = visibleProfiles,
                                modifier = Modifier.fillMaxSize(),
                                visibleCount = 3,
                                infiniteLoop = false,
                                onSwiped = { profile, direction ->
                                    when (direction) {
                                        DragValue.Left -> viewModel.onEvent(ProfileEvent.DislikeProfile(profile))
                                        DragValue.Right -> viewModel.onEvent(ProfileEvent.LikeProfile(profile))
                                        else -> {}
                                    }
                                },
                                onCardClicked = { profile ->
                                    viewModel.onEvent(ProfileEvent.SelectProfile(profile))
                                    navController.navigate(Screen.ProfileDetailsScreen.route)
                                }
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No more profiles!",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No more profiles!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
