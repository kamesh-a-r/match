# Match -

A modern Android dating application built with Jetpack Compose featuring a swipeable card stack interface for browsing profiles.

##  Features

- **Swipeable Card Stack**: Card interface with smooth swipe gestures
- **Profile Browsing**: Browse through profiles with like/dislike actions
- **Visual Feedback**: Real-time rotation and overlay effects during swipes
- **Stacking Effects**: Configurable card stacking with depth perception
- **Snackbar Notifications**: Instant feedback after each swipe action
- **Auto Data Initialization**: Dummy profiles automatically loaded on first app launch
- **Dual Profile Types**: Separate HOME and DAILY recommendation feeds
- **Clean Architecture**: Separation of concerns with MVI pattern
- **Unit Tests**: Comprehensive test coverage with MockK

##  Architecture

The app follows **Clean Architecture** principles with **MVI (Model-View-Intent)** pattern:

```
app/
├── data/                    # Data layer
│   ├── local/              # Local data sources (Room DB)
│   └── repository/         # Repository implementations
├── domain/                  # Domain layer
│   ├── model/              # Domain models
│   ├── repository/         # Repository interfaces
│   └── usecase/            # Business logic use cases
└── presentation/            # Presentation layer
    ├── cardStack/          # Card stack UI components
    │   ├── model/          # UI models (DragValue, StackFrom)
    │   └── view/           # Composables (SwipeableCard, TopStackedCardView)
    ├── home/               # Home screen
    └── profile/            # Profile screen
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

##  Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: Clean Architecture + MVI
- **Dependency Injection**: Hilt (assumed)
- **Database**: Room
- **Coroutines**: Kotlin Coroutines + Flow
- **Testing**: JUnit 4, MockK, Coroutines Test

## Dependencies

```kotlin
// Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.foundation:foundation")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")

// Room
implementation("androidx.room:room-runtime")
implementation("androidx.room:room-ktx")

// Testing
testImplementation("junit:junit:4.13.2")
testImplementation("io.mockk:mockk:1.13.8")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
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

The app automatically reloads fresh profile data every time it opens:

- **5 profiles**: Loaded from `AppDatabase.prepopulate()`
- **Fresh data on every launch**: Database is cleared and repopulated when app opens
- **Consistent experience**: Same profiles appear each time you restart the app
- **Both HOME and DAILY**: Same profiles available in both feeds

The initialization happens in the Room database `onOpen()` callback in `DatabaseModule.kt`.

**Behavior:**
-  Data resets every time you kill and reopen the app
-  Swipes are temporary - profiles return on app restart
-  Perfect for testing and demo purposes
-  No need to clear app data manually

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

##  Code Examples

### Basic Integration

```kotlin
@Composable
fun ProfileBrowser(profiles: List<Profile>) {
    var currentProfiles by remember { mutableStateOf(profiles) }
    
    TopStackedCardView(
        profiles = currentProfiles,
        onSwiped = { profile, direction ->
            // Handle swipe
            currentProfiles = currentProfiles.filter { it.id != profile.id }
        }
    )
}
```

### With ViewModel

```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    
    TopStackedCardView(
        profiles = state.profiles,
        onSwiped = { profile, direction ->
            when (direction) {
                DragValue.Left -> viewModel.dislikeProfile(profile)
                DragValue.Right -> viewModel.likeProfile(profile)
                else -> {}
            }
        },
        onCardClicked = { profile ->
            viewModel.selectProfile(profile)
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



