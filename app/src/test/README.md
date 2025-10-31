# Unit Tests for Match App

This directory contains comprehensive unit tests for the Match app following Clean Architecture with MVI pattern.

## Test Structure

```
test/
├── domain/
│   ├── model/
│   │   └── ProfileWithTypeTest.kt          # Model conversion tests
│   └── usecase/
│       ├── GetProfilesByTypeUseCaseTest.kt # Get profiles by type tests
│       ├── LikeProfileUseCaseTest.kt       # Like profile tests
│       └── RemoveProfileByTypeUseCaseTest.kt # Remove profile tests
├── data/
│   └── repository/
│       └── ProfileRepositoryImplTest.kt    # Repository implementation tests
└── presentation/
    ├── home/
    │   └── viewmodel/
    │       └── HomeViewModelTest.kt        # HomeViewModel tests
    └── profile/
        └── viewmodel/
            └── ProfileViewModelTest.kt     # ProfileViewModel tests
```

## Test Coverage

### Domain Layer Tests
- **Use Cases**: Tests for all use cases (GetProfilesByType, LikeProfile, DislikeProfile, RemoveProfileByType)
- **Models**: Tests for ProfileWithType conversion logic

### Data Layer Tests
- **Repository**: Tests for ProfileRepositoryImpl including:
  - Getting profiles by type (HOME/DAILY)
  - Like/Dislike operations
  - Remove operations

### Presentation Layer Tests
- **ViewModels**: Tests for both HomeViewModel and ProfileViewModel including:
  - Initial state verification
  - Profile loading by type
  - Event handling (SelectProfile, LikeProfile, DislikeProfile, ClearProfileSelection)
  - State updates
  - Separate data source verification

## Running Tests

### From Android Studio
1. Right-click on the `test` directory
2. Select "Run 'Tests in 'match.app.test''"

### From Command Line
```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.kamesh.match.presentation.home.viewmodel.HomeViewModelTest"

# Run with coverage
./gradlew testDebugUnitTest jacocoTestReport
```

## Test Dependencies

- **JUnit 4**: Testing framework
- **MockK**: Kotlin-first mocking framework (replaces Mockito)
- **Kotlinx Coroutines Test**: For testing coroutines
- **Turbine**: For testing Flows (optional)
- **AndroidX Core Testing**: For LiveData testing utilities

## Key Testing Patterns

### 1. ViewModel Testing
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
```

### 2. Use Case Testing with MockK
```kotlin
@Test
fun `invoke should return profiles for HOME type`() = runTest {
    // Given
    every { repository.getProfilesByType(ProfileType.HOME) } returns flowOf(testProfiles)
    
    // When
    val result = useCase(ProfileType.HOME).first()
    
    // Then
    assertEquals(testProfiles, result)
    verify { repository.getProfilesByType(ProfileType.HOME) }
}
```

### 3. Repository Testing with MockK
```kotlin
@Test
fun `getProfilesByType should return profiles for HOME type`() = runTest {
    // Given
    every { profileWithTypeDao.getProfilesByType(ProfileType.HOME) } returns flowOf(profilesWithType)
    
    // When
    val result = repository.getProfilesByType(ProfileType.HOME).first()
    
    // Then
    assertEquals(expectedProfiles, result)
    verify { profileWithTypeDao.getProfilesByType(ProfileType.HOME) }
}
```

## Test Scenarios Covered

### HomeViewModel
- ✅ Initial loading state
- ✅ Load HOME profiles on initialization
- ✅ Select profile updates state
- ✅ Like profile calls correct use cases
- ✅ Dislike profile calls correct use cases
- ✅ Clear profile selection

### ProfileViewModel
- ✅ Initial loading state
- ✅ Load DAILY profiles on initialization
- ✅ Select profile updates state
- ✅ Like profile with DAILY type
- ✅ Dislike profile with DAILY type
- ✅ Clear profile selection

### Repository
- ✅ Get all profiles
- ✅ Get profiles by type (HOME/DAILY)
- ✅ Remove profile by type
- ✅ Empty list handling

### Use Cases
- ✅ GetProfilesByType for both HOME and DAILY
- ✅ LikeProfile invocation
- ✅ DislikeProfile invocation
- ✅ RemoveProfileByType for both types

## Best Practices

1. **AAA Pattern**: Arrange, Act, Assert
2. **Test Naming**: Use descriptive names with backticks
3. **Mocking**: Mock dependencies with MockK, test behavior
4. **Coroutines**: Use `runTest` and `StandardTestDispatcher`
5. **Isolation**: Each test is independent
6. **Coverage**: Test happy paths and edge cases

## MockK Advantages

- **Kotlin-first**: Designed specifically for Kotlin
- **No JDK warnings**: No dynamic agent loading issues
- **DSL syntax**: Clean, readable mocking syntax
- **Coroutine support**: Built-in support for suspend functions with `coVerify`
- **Relaxed mocks**: Easy creation with `mockk(relaxed = true)`

## Notes

- All tests use **MockK** for mocking dependencies (Kotlin-native, no JDK warnings)
- Coroutine tests use `kotlinx-coroutines-test` library
- ViewModels use `StandardTestDispatcher` for deterministic testing
- Tests verify both state changes and use case invocations
- Use `coVerify` for suspend functions, `verify` for regular functions
