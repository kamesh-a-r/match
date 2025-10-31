package com.kamesh.match.presentation.details.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

/**
 * Profile Details Screen - Full Profile Information View
 * 
 * Displays comprehensive profile information when user taps on a profile card.
 * 
 * Features:
 * - Image carousel with multiple photos (auto-scroll disabled for better UX)
 * - Clean header with back navigation (no title to reduce clutter)
 * - Profile details: name, age, profession, location
 * - Verification badge for verified profiles
 * - Premium NRI indicator with golden highlight
 * - Star sign and religion information
 * - Height and other personal details
 * - Responsive layout with proper spacing and typography
 */
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kamesh.match.domain.model.Profile
import com.kamesh.match.presentation.profile.model.ProfileEvent
import com.kamesh.match.presentation.profile.model.ProfileStateProvider
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileDetailsScreen(navController: NavController, viewModel: ProfileStateProvider) {
    // Get current profile from ViewModel state
    val state by viewModel.state.collectAsState()
    val profile = state.selectedProfile

    // Clear selection when leaving this screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.onEvent(ProfileEvent.ClearProfileSelection)
        }
    }

    // Only show content if profile is selected
    if (profile != null) {
        Scaffold(
            // Simple header with back navigation only
            topBar = {
                TopAppBar(
                    title = {
                        // Empty title for cleaner look
                    },
                    navigationIcon = {
                        // Back button to return to previous screen
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.background(Color.White)
                )
            },
            containerColor = Color(0xFFF5F5F5) // Light gray background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image carousel showing multiple photos
                ProfileImageCarousel(profile = profile)

                Spacer(modifier = Modifier.height(24.dp))

                // Main profile information card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${profile.name}, ${profile.age}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333)
                            )
                            if (profile.isVerified) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = Color(0xFF20BF55),
                                    modifier = Modifier.padding(start = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = profile.profession,
                            fontSize = 16.sp,
                            color = Color(0xFF666666)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Star",
                                tint = Color(0xFFFFB703)
                            )
                            Text(
                                text = " ${profile.star} • ${profile.religion}",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Height: ${profile.height}",
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Location:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = profile.location,
                            fontSize = 14.sp,
                            color = Color(0xFF555555),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        if (profile.isPremiumNri) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFF01BAEF).copy(alpha = 0.15f))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Premium NRI Member",
                                    fontSize = 13.sp,
                                    color = Color(0xFF01BAEF),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Image Carousel Component
 * 
 * Displays profile photos in a swipeable horizontal pager:
 * - Shows attachments if available, otherwise falls back to main imageUrl
 * - Auto-scroll disabled for better user control
 * - Page indicators at bottom for navigation reference
 * - Smooth transition between images with proper aspect ratio
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileImageCarousel(profile: Profile) {
    // Use attachments if available, otherwise fallback to main image
    val images = remember(profile) {
        profile.attachments.ifEmpty { listOf(profile.imageUrl) }
    }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })

    // Auto-scroll disabled for better UX - users prefer manual control
//    LaunchedEffect(pagerState) {
//        while (true) {
//            delay(3500)
//            val nextPage = (pagerState.currentPage + 1) % images.size
//            pagerState.animateScrollToPage(nextPage)
//        }
//    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Profile image ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Page indicator
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(images.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == pagerState.currentPage) Color(0xFF20BF55)
                            else Color.LightGray
                        )
                )
            }
        }
    }
}
