package com.kamesh.match.presentation.widget.cardStack.view

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.kamesh.match.domain.model.Profile

@Composable
fun ProfileCard(
    profile: Profile,
    modifier: Modifier = Modifier,
    onDislike: () -> Unit = {},
    onLike: () -> Unit = {},
) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.height(400.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(profile.imageUrl),
                    contentDescription = profile.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "\uD83D\uDCF7 ${profile.photoCount}", color = Color.White)
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (profile.isVerified) {
                        Icon(
                            Icons.Filled.VerifiedUser,
                            contentDescription = "Verified",
                            tint = Color(0xFF46ABFC),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Verified", color = Color(0xFF46ABFC), fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (profile.isPremiumNri) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Premium NRI",
                            tint = Color(0xFFAC80F1),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Premium NRI", color = Color(0xFFAC80F1), fontSize = 10.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${profile.age} Yrs, ${profile.height}, ${profile.profession}, ${profile.star}, ${profile.religion}, ${profile.location}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(30.dp))
                HorizontalDivider(thickness = 0.5.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Star, contentDescription = "Shortlist", tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Shortlist")
                    }

                    Text(text = "Like her?")

                    Row {
                        IconButton(onClick = onDislike) { // Trigger swipe left
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Don't Like",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray.copy(alpha = 0.2f))
                                    .padding(8.dp),
                                tint = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onLike) { // Trigger swipe right
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Like",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFC107))
                                    .padding(8.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}