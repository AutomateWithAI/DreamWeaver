package com.example.dreamweaver

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GeneratingScreen(
    isDarkMode: Boolean,
    selectedMode: String,
    storyModes: List<StoryMode>,
    childNames: List<String>,
    manualCharacters: String,
    holidayName: String
) {
    val backgroundColor = if (isDarkMode) {
        Brush.radialGradient(
            listOf(Color(0xFF1E293B), Color(0xFF1E3A8A), Color(0xFF312E81))
        )
    } else {
        Brush.radialGradient(
            listOf(Color(0xFFF8FAFC), Color(0xFFDDEAFE), Color(0xFFE0E7FF))
        )
    }

    val characters = manualCharacters.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    val charactersText = if (characters.size > 2) {
        "${characters.take(2).joinToString(" and ")} and others"
    } else {
        characters.joinToString(" and ")
    }
    
    val validNames = childNames.filter { it.trim().isNotEmpty() }
    val childNamesText = when {
        validNames.isEmpty() -> "your child"
        validNames.size == 1 -> validNames[0]
        validNames.size == 2 -> "${validNames[0]} and ${validNames[1]}"
        else -> "${validNames.dropLast(1).joinToString(", ")}, and ${validNames.last()}"
    }
    
    val selectedModeData = storyModes.find { it.id == selectedMode }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Color(0xFF1E293B).copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Brush.linearGradient(
                                selectedModeData?.gradientColors ?: listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                            ),
                            RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        selectedModeData?.icon ?: Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    "Creating $childNamesText's Story...",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color(0xFF1E293B),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    "Crafting a ${selectedModeData?.name?.lowercase() ?: "story"} with $charactersText" +
                            if (selectedMode == "holiday" && holidayName.isNotEmpty()) " for $holidayName" else "",
                    fontSize = 16.sp,
                    color = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF64748B),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Progress indicators
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProgressStep(
                        text = "Writing your story...",
                        isDarkMode = isDarkMode,
                        dotColor = Color(0xFF6366F1)
                    )
                    ProgressStep(
                        text = "Adding special moments...",
                        isDarkMode = isDarkMode,
                        dotColor = Color(0xFF8B5CF6)
                    )
                    ProgressStep(
                        text = "Personalizing for $childNamesText...",
                        isDarkMode = isDarkMode,
                        dotColor = Color(0xFFEC4899)
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressStep(
    text: String,
    isDarkMode: Boolean,
    dotColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isDarkMode) Color(0xFF475569).copy(alpha = 0.5f) else Color(0xFFF1F5F9),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(dotColor, androidx.compose.foundation.shape.CircleShape)
        )
        Text(
            text,
            fontSize = 14.sp,
            color = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF64748B)
        )
    }
}

@Composable
fun StoryScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    generatedStory: String,
    selectedMode: String,
    storyModes: List<StoryMode>,
    childNames: List<String>,
    holidayName: String,
    manualCharacters: String,
    onCreateAnother: () -> Unit,
    onStartOver: () -> Unit
) {
    val backgroundColor = if (isDarkMode) {
        Brush.radialGradient(
            listOf(Color(0xFF1E293B), Color(0xFF1E3A8A), Color(0xFF312E81))
        )
    } else {
        Brush.radialGradient(
            listOf(Color(0xFFF8FAFC), Color(0xFFDDEAFE), Color(0xFFE0E7FF))
        )
    }

    val characters = manualCharacters.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    val validNames = childNames.filter { it.trim().isNotEmpty() }
    val childNamesText = when {
        validNames.isEmpty() -> "Your Child"
        validNames.size == 1 -> validNames[0]
        validNames.size == 2 -> "${validNames[0]} and ${validNames[1]}"
        else -> "${validNames.dropLast(1).joinToString(", ")}, and ${validNames.last()}"
    }
    
    val selectedModeData = storyModes.find { it.id == selectedMode }
    
    fun generateStoryTitle(): String {
        return when (selectedMode) {
            "playful" -> "$childNamesText's Fun Adventure"
            "bathtime" -> "$childNamesText's Splashing Bath Time"
            "bedtime" -> "$childNamesText's Peaceful Dream"
            "holiday" -> if (holidayName.isNotEmpty()) "$childNamesText's $holidayName Adventure" else "$childNamesText's Holiday Magic"
            else -> "$childNamesText's Special Story"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    Brush.linearGradient(
                                        selectedModeData?.gradientColors ?: listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
                                    ),
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                selectedModeData?.icon ?: Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            "$childNamesText's Story",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) Color.White else Color(0xFF1E293B),
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            selectedModeData?.name ?: "Your Story" + 
                                    if (selectedMode == "holiday" && holidayName.isNotEmpty()) " - $holidayName" else "",
                            fontSize = 16.sp,
                            color = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF64748B),
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    IconButton(
                        onClick = onToggleDarkMode,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isDarkMode) Color(0xFF334155) else Color.White.copy(alpha = 0.8f)
                            )
                    ) {
                        Icon(
                            if (isDarkMode) Icons.Default.WbSunny else Icons.Default.NightlightRound,
                            contentDescription = "Toggle dark mode",
                            tint = if (isDarkMode) Color(0xFFFBBF24) else Color(0xFF64748B)
                        )
                    }
                }
            }
            
            item {
                // Story Content Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF1E293B).copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        // Story Title
                        Text(
                            generateStoryTitle(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B5CF6),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        )
                        
                        // Story Text
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            generatedStory.split("\n\n").forEach { paragraph ->
                                if (paragraph.trim().isNotEmpty()) {
                                    Text(
                                        paragraph.trim(),
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        color = if (isDarkMode) Color.White else Color(0xFF1E293B)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                // Character Tags Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF1E293B).copy(alpha = 0.6f) else Color.White.copy(alpha = 0.6f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isDarkMode) Color(0xFF475569).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Featuring:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF64748B),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        // Character chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                        ) {
                            characters.take(4).forEach { char ->
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isDarkMode) Color(0xFF6366F1).copy(alpha = 0.2f) else Color(0xFF6366F1).copy(alpha = 0.1f)
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isDarkMode) Color(0xFF6366F1).copy(alpha = 0.5f) else Color(0xFF6366F1).copy(alpha = 0.3f)
                                    )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "✨",
                                            fontSize = 12.sp
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            char,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isDarkMode) Color(0xFF93C5FD) else Color(0xFF6366F1)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                // Action Buttons
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Create Another Story Button
                    Button(
                        onClick = onCreateAnother,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF8B5CF6), Color(0xFF6366F1))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Text(
                                    "Create Another Story",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    
                    // Start Over Button
                    OutlinedButton(
                        onClick = onStartOver,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isDarkMode) Color(0xFF1E293B).copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isDarkMode) Color(0xFF475569) else Color(0xFFE2E8F0)
                        )
                    ) {
                        Text(
                            "Start Over",
                            color = if (isDarkMode) Color.White else Color(0xFF1E293B),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
