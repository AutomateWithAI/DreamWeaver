# DreamWeaver Android App

An Android version of the DreamWeaver children's story generator app, built with Jetpack Compose and Kotlin.

## Features

### Complete Story Generation Flow
- **Welcome Screen**: Beautiful landing page with app introduction
- **Character Selection**: Choose from popular franchises or create custom characters
- **Story Customization**: Add children's names, select story types, and holiday themes
- **Story Generation**: Animated loading screen with progress indicators
- **Story Display**: Formatted story with character tags and action buttons

### Story Types
- **Playful Stories**: Fun adventures for anytime play
- **Bath Time Stories**: Splash into fun bath time tales
- **Bedtime Stories**: Gentle tales for peaceful sleep
- **Holiday Stories**: Festive adventures for special occasions

### Popular Character Franchises
- Super Mario Bros (Mario, Luigi, Princess Peach, Bowser, etc.)
- Pokemon (Pikachu, Charizard, Squirtle, Bulbasaur)
- Disney Classics (Mickey Mouse, Minnie Mouse, Donald Duck, Goofy)
- Paw Patrol (Chase, Marshall, Skye, Rubble)
- Minecraft (Steve, Alex, Ender Dragon, Creepers, Chip, Milo, etc.)
- Bluey (Bluey, Bingo, Bandit, Chilli, Muffin, Socks)

### Technical Features
- **Modern UI**: Built with Jetpack Compose and Material Design 3
- **Dark/Light Mode**: Toggle between themes with smooth transitions
- **Responsive Design**: Optimized for mobile screens
- **State Management**: Proper state handling across screens
- **Navigation**: Smooth screen transitions and back navigation
- **Story Generation**: Local story generation with customizable templates

### Architecture
- **MVVM Pattern**: Clean separation of concerns
- **Compose Navigation**: Modern Android navigation
- **Material Design 3**: Latest design system
- **Kotlin Coroutines**: Async story generation
- **State Management**: Reactive UI updates

## Project Structure
```
app/src/main/java/com/example/dreamweaver/
├── MainActivity.kt              # Main activity and app logic
├── WelcomeScreen.kt            # Landing page screen
├── CharacterInputScreen.kt     # Character selection screen
├── CustomizeScreen.kt          # Story customization screen
├── GeneratingScreen.kt         # Loading and story screens
└── ui/theme/                   # Theme and styling
    ├── Theme.kt
    ├── Color.kt
    └── Type.kt
```

## Key Components

### Data Models
- `StoryMode`: Represents different story types with icons and descriptions
- `CharacterFranchise`: Popular character collections with themes

### Screen Components
- `WelcomeScreen`: App introduction and story type preview
- `CharacterInputScreen`: Character selection with popular franchises and custom input
- `CustomizeScreen`: Children's names, story mode selection, and holiday input
- `GeneratingScreen`: Animated loading with progress steps
- `StoryScreen`: Final story display with formatting and actions

### Features Implemented
- ✅ Multi-step story creation flow
- ✅ Character franchise selection
- ✅ Custom character input
- ✅ Multiple children names support
- ✅ Story type selection (Playful, Bath Time, Bedtime, Holiday)
- ✅ Holiday-specific customization
- ✅ Dark/Light mode toggle
- ✅ Animated loading states
- ✅ Story generation with templates
- ✅ Character tags display
- ✅ Create another story functionality
- ✅ Complete app reset

## Build Instructions

1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to the `AndroidProject` folder
4. Let Gradle sync complete
5. Run on emulator or device (API 24+)

## Dependencies
- Jetpack Compose BOM 2023.10.01
- Material Design 3
- Material Icons Extended
- Navigation Compose
- Activity Compose
- Kotlin Coroutines

## Minimum Requirements
- Android API 24 (Android 7.0)
- Target SDK 34
- Kotlin 1.9.0
- Android Gradle Plugin 8.1.0

The app provides the same core functionality as the React version but optimized for Android with native performance and Material Design aesthetics.
