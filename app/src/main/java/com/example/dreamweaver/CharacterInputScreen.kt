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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterInputScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onBack: () -> Unit,
    popularCharacters: List<CharacterFranchise>,
    manualShow: String,
    onManualShowChange: (String) -> Unit,
    manualCharacters: String,
    onManualCharactersChange: (String) -> Unit,
    onContinue: () -> Unit,
    onQuickSelect: (CharacterFranchise) -> Unit
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isDarkMode) Color(0xFF334155) else Color.White.copy(alpha = 0.8f)
                                )
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF64748B)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                "Choose Characters",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkMode) Color.White else Color(0xFF1E293B)
                            )
                            Text(
                                "Who would you like in the story?",
                                fontSize = 16.sp,
                                color = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF64748B)
                            )
                        }
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
                // Popular Characters and Custom Input Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            "Popular Characters",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkMode) Color.White else Color(0xFF1E293B),
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                        
                        // Character Franchises
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            popularCharacters.forEach { franchise ->
                                Card(
                                    onClick = { onQuickSelect(franchise) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .background(
                                                        Brush.linearGradient(franchise.gradientColors),
                                                        RoundedCornerShape(12.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    franchise.icon,
                                                    fontSize = 20.sp
                                                )
                                            }
                                            
                                            Spacer(modifier = Modifier.width(12.dp))
                                            
                                            Column {
                                                Text(
                                                    franchise.name,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (isDarkMode) Color.White else Color(0xFF1E293B)
                                                )
                                                Text(
                                                    franchise.characters.take(3).joinToString(", ") +
                                                            if (franchise.characters.size > 3) " +${franchise.characters.size - 3} more" else "",
                                                    fontSize = 14.sp,
                                                    color = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF64748B)
                                                )
                                            }
                                        }
                                        
                                        // Radio button
                                        RadioButton(
                                            selected = manualShow == franchise.name,
                                            onClick = { onQuickSelect(franchise) }
                                        )
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Custom Input Section
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    "Create Your Own",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDarkMode) Color.White else Color(0xFF1E293B),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Show/Franchise Input
                                    Column {
                                        Text(
                                            "Show/Franchise",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isDarkMode) Color.White else Color(0xFF1E293B),
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        OutlinedTextField(
                                            value = manualShow,
                                            onValueChange = onManualShowChange,
                                            placeholder = {
                                                Text("e.g., My Little Pony, Minecraft, etc.")
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                    }
                                    
                                    // Characters Input
                                    Column {
                                        Text(
                                            "Characters",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isDarkMode) Color.White else Color(0xFF1E293B),
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        OutlinedTextField(
                                            value = manualCharacters,
                                            onValueChange = onManualCharactersChange,
                                            placeholder = {
                                                Text("e.g., Rainbow Dash, Twilight Sparkle, Fluttershy")
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        Text(
                                            "Separate multiple characters with commas",
                                            fontSize = 12.sp,
                                            color = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF64748B),
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                // Continue Button
                Button(
                    onClick = onContinue,
                    enabled = manualCharacters.trim().isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Continue with These Characters",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
