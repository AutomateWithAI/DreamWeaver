package com.example.dreamweaver

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/** Data model representing a story mode option */
data class StoryMode(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val gradientColors: List<Color>
)

/** Collection of popular characters grouped by franchise */
data class CharacterFranchise(
    val name: String,
    val icon: String,
    val characters: List<String>,
    val gradientColors: List<Color>
)

/** Default story mode options used throughout the app */
val DefaultStoryModes = listOf(
    StoryMode(
        id = "playful",
        name = "Playful",
        icon = Icons.Default.AutoAwesome,
        gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF6366F1))
    ),
    StoryMode(
        id = "bathtime",
        name = "Bath Time",
        icon = Icons.Default.Opacity,
        gradientColors = listOf(Color(0xFF3B82F6), Color(0xFF06B6D4))
    ),
    StoryMode(
        id = "bedtime",
        name = "Bedtime",
        icon = Icons.Default.Hotel,
        gradientColors = listOf(Color(0xFF6366F1), Color(0xFF4338CA))
    ),
    StoryMode(
        id = "holiday",
        name = "Holiday",
        icon = Icons.Default.Cake,
        gradientColors = listOf(Color(0xFFF472B6), Color(0xFFFDE047))
    )
)

/** Default list of popular character franchises */
val DefaultCharacterFranchises = listOf(
    CharacterFranchise(
        name = "Super Mario Bros",
        icon = "\uD83C\uDF1F",
        characters = listOf("Mario", "Luigi", "Peach", "Bowser"),
        gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFEF4444))
    ),
    CharacterFranchise(
        name = "Pokemon",
        icon = "\u26A1",
        characters = listOf("Pikachu", "Charizard", "Squirtle", "Bulbasaur"),
        gradientColors = listOf(Color(0xFFFACC15), Color(0xFFF59E0B))
    ),
    CharacterFranchise(
        name = "Disney Classics",
        icon = "\uD83C\uDF89",
        characters = listOf("Mickey", "Minnie", "Donald", "Goofy"),
        gradientColors = listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))
    ),
    CharacterFranchise(
        name = "Paw Patrol",
        icon = "\uD83D\uDC15",
        characters = listOf("Chase", "Marshall", "Skye", "Rubble"),
        gradientColors = listOf(Color(0xFF3B82F6), Color(0xFF06B6D4))
    ),
    CharacterFranchise(
        name = "Minecraft",
        icon = "\u26F1",
        characters = listOf("Steve", "Alex", "Ender Dragon", "Creeper", "Chip", "Milo"),
        gradientColors = listOf(Color(0xFF34D399), Color(0xFF10B981))
    ),
    CharacterFranchise(
        name = "Bluey",
        icon = "\uD83D\uDC3E",
        characters = listOf("Bluey", "Bingo", "Bandit", "Chilli"),
        gradientColors = listOf(Color(0xFF60A5FA), Color(0xFF2563EB))
    )
)
