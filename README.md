# Match

A modern Android dating application built with Jetpack Compose featuring dual navigation experiences: horizontal scrollable cards and interactive swipeable card stacks for browsing profiles.

## Features

### User Experience
- **Dual Screen Design**: Home screen with horizontal scrolling cards + Profile screen with swipeable card stack
- **Smart Profile Management**: Separate profile states for HOME and DAILY screens with independent like/dislike actions
- **Interactive Gestures**: Swipe left to dislike, right to like with smooth animations and visual feedback
- **Comprehensive Navigation**: Seamless transitions between list view, detail view, and daily matches
- **Personalized Feedback**: Custom snackbar messages with user names and emojis

### Technical Features
- **Modern Architecture**: Clean Architecture with MVVM pattern and reactive programming
- **Database Integration**: Room database with automatic demo data population and state management
- **Dependency Injection**: Hilt for clean dependency management across all layers
- **Reactive UI**: StateFlow/Flow-based state management with Compose integration
- **Comprehensive Testing**: Unit tests with MockK, Turbine, and Coroutines Test
- **Type Safety**: Sealed classes for events and strong typing throughout

## Architecture

The app follows **Clean Architecture** principles with **MVVM** pattern and reactive state management:

```
app/
├── data/                           # Data layer
│   ├── local/                     # Local data sources (Room DB)
│   │   ├── AppDatabase.kt         # Room database with auto-population
│   │   ├── ProfileDao.kt          # Basic profile operations
│   │   ├── ProfileWithTypeDao.kt  # Screen-specific profile operations
│   │   └── Converters.kt          # Type converters for Room
│   └── repository/                # Repository implementations
│       └── ProfileRepositoryImpl.kt
├── domain/                         # Domain layer
│   ├── model/                     # Domain models
│   │   ├── Profile.kt             # Core profile entity
│   │   ├── ProfileType.kt         # HOME/DAILY screen types
│   │   └── ProfileWithType.kt     # Screen-specific profile data
│   ├── repository/                # Repository interfaces
│   │   └── ProfileRepository.kt
│   └── usecase/                   # Business logic use cases
│       ├── ProfileUseCases.kt     # Use case facade
│       ├── GetProfilesByTypeUseCase.kt
│       ├── LikeProfileUseCase.kt
│       └── [Other use cases]
├── presentation/                   # Presentation layer
│   ├── home/                      # Home screen (horizontal cards)
│   │   ├── components/            # Home-specific components
│   │   ├── view/                  # Home screen composable
│   │   └── viewmodel/             # Home ViewModel
│   ├── profile/                   # Profile screen (swipeable stack)
│   │   ├── model/                 # Profile events & state
│   │   ├── view/                  # Profile screen composable
│   │   └── viewmodel/             # Profile ViewModel
│   ├── details/                   # Profile detail view
│   │   └── view/                  # Detail screen composable
│   ├── widget/cardStack/          # Reusable card stack components
│   │   ├── model/                 # DragValue, StackFrom enums
│   │   └── view/                  # SwipeableCard, TopStackedCardView
│   └── navigation/                # App navigation
│       ├── NavGraph.kt            # Navigation setup
│       └── Screen.kt              # Screen definitions
└── di/                            # Dependency injection
    ├── DatabaseModule.kt          # Database providers
    └── RepositoryModule.kt        # Repository & use case providers
```

## Card Stack Component

### Overview

The card stack component provides a swipeable interface for browsing profiles. It features:

- **Gesture-based Interaction**: Swipe left to dislike, right to like
- **Visual Depth**: Cards stack with scale and translation effects
- **Smooth Animations**: Rotation and position animations during swipes
- **Flexible Configuration**: Customizable stacking direction, spacing, and thresholds

### Components

#### 1. TopStackedCardView

The main composable that manages the card stack.

**Key Features:**
- Manages visible cards and their lifecycle
- Handles swipe callbacks and state management
- Displays snackbar feedback
- Supports infinite loop or finite mode
- Configurable stacking direction (Top, Bottom, Left, Right, None)

**Usage Example:**

```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsState()
    var profiles by remember { mutableStateOf(state.profiles) }

    TopStackedCardView(
        profiles = profiles,
        visibleCount = 3,
        infiniteLoop = false,
        stackFrom = StackFrom.Top,
        translationInterval = 8.dp,
        scaleInterval = 0.95f,
        swipeThreshold = 0.3f,
        onSwiped = { profile, direction ->
            when (direction) {
                DragValue.Left -> viewModel.onEvent(HomeEvent.DislikeProfile(profile))
                DragValue.Right -> viewModel.onEvent(HomeEvent.LikeProfile(profile))
                else -> {}
            }
            // Remove swiped profile from list
            profiles = profiles.filter { it.id != profile.id }
        },
        onAllCardsSwiped = {
            // Load more profiles or show completion
            viewModel.loadMoreProfiles()
        },
        onCardClicked = { profile ->
            // Navigate to profile detail
            viewModel.onEvent(HomeEvent.SelectProfile(profile))
        }
    )
}
```

**Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `profiles` | `List<Profile>` | Required | List of profiles to display |
| `modifier` | `Modifier` | `Modifier` | Modifier for root container |
| `initialTopIndex` | `Int` | `0` | Starting index of top card |
| `visibleCount` | `Int` | `3` | Number of visible cards in stack |
| `infiniteLoop` | `Boolean` | `false` | Enable infinite cycling |
| `stackFrom` | `StackFrom` | `StackFrom.Top` | Stacking direction |
| `translationInterval` | `Dp` | `8.dp` | Spacing between cards |
| `scaleInterval` | `Float` | `0.95f` | Scale reduction per card |
| `swipeThreshold` | `Float` | `0.3f` | Swipe trigger threshold (0.0-1.0) |
| `onSwiped` | `(Profile, DragValue) -> Unit` | `{}` | Swipe callback |
| `onAllCardsSwiped` | `() -> Unit` | `{}` | All cards swiped callback |
| `onCardClicked` | `(Profile) -> Unit` | `{}` | Card click callback |

#### 2. SwipeableCard

Individual card component with swipe gesture handling.

**Key Features:**
- Horizontal drag gestures with `AnchoredDraggableState`
- Rotation animation based on drag offset
- Overlay indicators (like/dislike) during swipe
- Configurable velocity and positional thresholds
- Click handling for profile details

**Technical Details:**
- Uses `AnchoredDraggableState` for gesture management
- Three anchor points: Left (-1000f), Center (0f), Right (1000f)
- Exponential decay animation for fling behavior
- Z-index based layering for proper stacking

#### 3. StackFrom Enum

Defines the stacking direction for cards:

```kotlin
enum class StackFrom {
    None,    // No stacking effect
    Top,     // Cards stack from top (peek from above)
    Bottom,  // Cards stack from bottom (peek from below)
    Left,    // Cards stack from left
    Right    // Cards stack from right
}
```

#### 4. DragValue Enum

Represents the card's position state:

```kotlin
enum class DragValue {
    Left,    // Swiped left (dislike)
    Center,  // At center position
    Right    // Swiped right (like)
}
```

### Implementation Notes

#### State Management
- Parent composable should manage the profiles list
- Remove swiped profiles in the `onSwiped` callback
- Keep `initialTopIndex` constant as list shrinks

#### Z-Index Ordering
- Cards are rendered in reverse order for proper layering
- Top card has highest z-index (drawn last)
- Snackbar uses `Float.MAX_VALUE` to appear above all cards

#### Performance
- Uses `key(profile.id)` for efficient recomposition
- Only visible cards are rendered (configurable via `visibleCount`)
- Animations use hardware acceleration via `graphicsLayer`

#### Gesture Thresholds
- `positionalThreshold`: Fraction of drag distance to trigger swipe (default: 30%)
- `velocityThreshold`: Minimum fling velocity in pixels (default: 400dp)
- Cards snap back to center if thresholds aren't met

## Tech Stack

- **Language**: Kotlin 2.2.21
- **UI**: Jetpack Compose 2025.10.01 (Material 3)
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Hilt 2.57.2
- **Database**: Room 2.8.3
- **Image Loading**: Coil 2.7.0
- **Coroutines**: Kotlin Coroutines + Flow
- **Testing**: JUnit 4, MockK 1.14.6, Turbine 1.2.1, Coroutines Test

## Key Dependencies

Based on `gradle/libs.versions.toml`:

```toml
[versions]
kotlin = "2.2.21"
composeBom = "2025.10.01"
hilt = "2.57.2"
room = "2.8.3"
coil = "2.7.0"
mockk = "1.14.6"
gson = "2.13.2"
turbine = "1.2.1"

[libraries]
# Compose BOM
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }

# Room
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }

# Image Loading
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }

# Testing
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
turbine = { group = "app.cash.turbine", name = "turbine", version.ref = "turbine" }
```

## Testing

Comprehensive unit tests with MockK for all layers:

- **Domain Layer**: Use case tests
- **Data Layer**: Repository tests
- **Presentation Layer**: ViewModel tests

Run tests:
```bash
# All tests
./gradlew test

# Specific test class
./gradlew test --tests "com.kamesh.match.presentation.home.viewmodel.HomeViewModelTest"

# With coverage
./gradlew testDebugUnitTest jacocoTestReport
```

See [Test README](app/src/test/README.md) for detailed testing documentation.

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17 or later
- Android SDK 24+

### Setup
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run the app on an emulator or device

### Data Initialization

The app automatically manages profile data across two distinct screens:

**Database Strategy:**
- **Two-table design**: `profiles` table (master data) + `profile_with_type` table (screen-specific copies)
- **Screen independence**: HOME and DAILY screens maintain separate profile states
- **Fresh data on launch**: Database cleared and repopulated on every app open via `DatabaseModule.onOpen()`
- **5 demo profiles**: Ananya, Rachel, Priya, Neha, Sofia with varied characteristics

**Profile Distribution:**
- **HOME screen**: Horizontal scrollable cards with live count indicators
- **DAILY screen**: Swipeable card stack for focused matching
- **Independent actions**: Liking/disliking on one screen doesn't affect the other
- **Persistent until restart**: Changes persist until you kill and restart the app

**Demo Features:**
- ✅ Consistent demo experience - same profiles every restart
- ✅ No data corruption - fresh state guaranteed
- ✅ Easy testing - no manual data clearing needed
- ✅ Premium indicators - some profiles marked as "Premium NRI"

### Configuration

Customize the card stack behavior in your composable:

```kotlin
TopStackedCardView(
    profiles = profiles,
    visibleCount = 5,              // Show more cards for deeper stack
    stackFrom = StackFrom.Bottom,  // Stack from bottom instead of top
    translationInterval = 12.dp,   // More spacing between cards
    scaleInterval = 0.92f,         // More dramatic scale reduction
    swipeThreshold = 0.25f,        // Easier to trigger swipes
    // ... callbacks
)
```

## Screen Examples

### Home Screen (Horizontal Cards)

The home screen displays profiles in horizontal scrollable cards with live counts:

```kotlin
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsState()
    
    // Header shows live profile count
    Text("${state.profiles.size} Profiles pending with me")
    
    // Premium indicator badge  
    Text("${state.profiles.count { it.isPremiumNri }} NEW")
    
    LazyRow {
        items(state.profiles, key = { it.id }) { profile ->
            HomeProfileCard(
                profile = profile,
                onLike = {
                    // Show personalized snackbar
                    snackbarHostState.showSnackbar("💖 Great choice! ${profile.name} has been liked")
                    viewModel.onEvent(ProfileEvent.LikeProfile(profile))
                },
                onDislike = {
                    snackbarHostState.showSnackbar("👋 No worries! You passed on ${profile.name}")
                    viewModel.onEvent(ProfileEvent.DislikeProfile(profile))
                },
                onClick = {
                    // Navigate to detail view
                    viewModel.onEvent(ProfileEvent.SelectProfile(profile))
                    navController.navigate(Screen.ProfileDetailsScreen.route)
                }
            )
        }
    }
}
```

### Profile Screen (Swipeable Stack)

The daily matches screen uses the swipeable card stack:

```kotlin
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    val state by viewModel.state.collectAsState()
    
    TopStackedCardView(
        profiles = state.profiles,
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
}
```

### Infinite Loop Mode

```kotlin
TopStackedCardView(
    profiles = profiles,
    infiniteLoop = true,  // Cards cycle endlessly
    onSwiped = { profile, direction ->
        // Handle swipe without removing from list
        saveSwipeAction(profile, direction)
    }
)
```

##  Customization

### Custom Stacking Direction

```kotlin
// Stack from bottom (cards peek from below)
TopStackedCardView(
    profiles = profiles,
    stackFrom = StackFrom.Bottom,
    translationInterval = 10.dp
)
```

### Adjust Swipe Sensitivity

```kotlin
// Make swipes easier to trigger
TopStackedCardView(
    profiles = profiles,
    swipeThreshold = 0.2f  // 20% of drag distance
)
```

### Custom Visual Effects

Modify `SwipeableCard.kt` to customize:
- Rotation angle calculation (line 127-133)
- Overlay alpha and colors (line 200-212)
- Scale and translation effects (line 116-124)



