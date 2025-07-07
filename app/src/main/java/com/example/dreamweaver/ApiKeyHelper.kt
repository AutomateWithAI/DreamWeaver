package com.example.dreamweaver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyHelpDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Help,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("How to Get an OpenAI API Key")
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Follow these steps to get your OpenAI API key:",
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val steps = listOf(
                    "1. Go to platform.openai.com" to "Visit the OpenAI platform website",
                    "2. Sign up or Log in" to "Create an account or use existing one",
                    "3. Go to API Keys" to "Navigate to your dashboard → API Keys",
                    "4. Create new key" to "Click 'Create new secret key'",
                    "5. Copy your key" to "Copy the key that starts with 'sk-'",
                    "6. Paste here" to "Enter it in the app's API key field"
                )
                
                steps.forEach { (step, description) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = step.split(".")[0],
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Column {
                            Text(
                                text = step.split(". ")[1],
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Important Notes:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val notes = listOf(
                            "💳 You'll need to add a payment method to OpenAI",
                            "💰 Stories cost ~$0.0007 each (less than 1 cent)",
                            "🔒 Your API key is stored locally and securely",
                            "🔄 You can use template stories without an API key"
                        )
                        
                        notes.forEach { note ->
                            Text(
                                text = note,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it!")
            }
        }
    )
}

@Composable
fun ApiKeyStatusCard(
    isValid: Boolean,
    isTested: Boolean,
    isAiEnabled: Boolean,
    onHelpClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isAiEnabled && isValid && isTested -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                isAiEnabled && isValid -> Color(0xFFFF9800).copy(alpha = 0.1f)
                isAiEnabled -> Color(0xFFF44336).copy(alpha = 0.1f)
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    when {
                        isAiEnabled && isValid && isTested -> Icons.Default.CheckCircle
                        isAiEnabled && isValid -> Icons.Default.Warning
                        isAiEnabled -> Icons.Default.Error
                        else -> Icons.Default.AutoAwesome
                    },
                    contentDescription = null,
                    tint = when {
                        isAiEnabled && isValid && isTested -> Color(0xFF4CAF50)
                        isAiEnabled && isValid -> Color(0xFFFF9800)
                        isAiEnabled -> Color(0xFFF44336)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = when {
                        isAiEnabled && isValid && isTested -> "AI Ready! ✨"
                        isAiEnabled && isValid -> "API Key Valid - Click Test"
                        isAiEnabled -> "API Key Needed"
                        else -> "Template Mode"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            if (isAiEnabled) {
                IconButton(
                    onClick = onHelpClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.HelpOutline,
                        contentDescription = "API Key Help",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
