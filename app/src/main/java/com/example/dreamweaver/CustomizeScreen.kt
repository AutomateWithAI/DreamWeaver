package com.example.dreamweaver

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dreamweaver.service.ApiKeyTestResult
import com.example.dreamweaver.service.ApiKeyValidation
import com.example.dreamweaver.service.StoryGeneratorService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    selectedCharacters: List<String>,
    customCharacters: String,
    childNames: List<String>,
    selectedMode: String,
    holidayName: String,
    apiKey: String,
    onChildNamesUpdate: (List<String>) -> Unit,
    onModeSelect: (String) -> Unit,
    onHolidayUpdate: (String) -> Unit,
    onApiKeyUpdate: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateStoryClick: () -> Unit
) {
    var showApiKeyInput by remember { mutableStateOf(false) }
    var showApiKey by remember { mutableStateOf(false) }
    var isTestingApiKey by remember { mutableStateOf(false) }
    var apiKeyTestResult by remember { mutableStateOf<ApiKeyTestResult?>(null) }
    var apiKeyValidation by remember { mutableStateOf(ApiKeyValidation.EMPTY) }
    var showHelpDialog by remember { mutableStateOf(false) }
    
    val storyGenerator = remember { StoryGeneratorService() }
    val coroutineScope = rememberCoroutineScope()
    
    // Validate API key whenever it changes
    LaunchedEffect(apiKey) {
        apiKeyValidation = storyGenerator.validateApiKey(apiKey)
        if (apiKey.isNotBlank() && apiKeyValidation == ApiKeyValidation.VALID) {
            apiKeyTestResult = null // Reset test result when key changes
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            
            Text(
                text = "Customize Your Story",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            IconButton(onClick = onToggleDarkMode) {
                Icon(
                    if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            
            // Your Characters Section
            Text(
                text = "Your Characters",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val displayCharacters = if (selectedCharacters.isNotEmpty()) {
                    selectedCharacters
                } else if (customCharacters.isNotBlank()) {
                    customCharacters.split(",").map { it.trim() }.filter { it.isNotBlank() }
                } else {
                    listOf("No characters selected")
                }
                
                displayCharacters.forEach { character ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = character,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Children's Names Section
            Text(
                text = "Children's Names",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            childNames.forEachIndexed { index, name ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { newName ->
                            val updatedNames = childNames.toMutableList()
                            updatedNames[index] = newName
                            onChildNamesUpdate(updatedNames)
                        },
                        label = { Text("Child ${index + 1}") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    
                    if (childNames.size > 1) {
                        IconButton(
                            onClick = {
                                val updatedNames = childNames.toMutableList()
                                updatedNames.removeAt(index)
                                onChildNamesUpdate(updatedNames)
                            }
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Remove")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            if (childNames.size < 3) {
                OutlinedButton(
                    onClick = {
                        onChildNamesUpdate(childNames + "")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Another Child")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Story Type Section
            Text(
                text = "Story Type",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val storyModes = listOf("Playful", "Bath Time", "Bedtime", "Holiday")
            
            storyModes.forEach { mode ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onModeSelect(mode) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedMode == mode,
                        onClick = { onModeSelect(mode) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mode,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            
            // Holiday Input (only show if Holiday mode selected)
            if (selectedMode == "Holiday") {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = holidayName,
                    onValueChange = onHolidayUpdate,
                    label = { Text("Holiday Name (e.g., Christmas, Birthday)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // API Key Status Card
            ApiKeyStatusCard(
                isValid = apiKeyValidation == ApiKeyValidation.VALID,
                isTested = apiKeyTestResult == ApiKeyTestResult.SUCCESS,
                isAiEnabled = showApiKeyInput,
                onHelpClick = { showHelpDialog = true }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // AI Settings Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AI Story Generation",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Switch(
                            checked = showApiKeyInput,
                            onCheckedChange = { 
                                showApiKeyInput = it
                                if (!it) {
                                    onApiKeyUpdate("")
                                    apiKeyTestResult = null
                                }
                            }
                        )
                    }
                    
                    if (showApiKeyInput) {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Enter your OpenAI API key for AI-generated stories",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = apiKey,
                            onValueChange = { newKey ->
                                onApiKeyUpdate(newKey)
                                apiKeyTestResult = null
                            },
                            label = { Text("OpenAI API Key") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                Row {
                                    // Test API Key Button
                                    if (apiKey.isNotBlank() && apiKeyValidation == ApiKeyValidation.VALID && apiKeyTestResult == null) {
                                        IconButton(
                                            onClick = {
                                                coroutineScope.launch {
                                                    isTestingApiKey = true
                                                    apiKeyTestResult = storyGenerator.testApiKey(apiKey)
                                                    isTestingApiKey = false
                                                }
                                            },
                                            enabled = !isTestingApiKey
                                        ) {
                                            if (isTestingApiKey) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(16.dp),
                                                    strokeWidth = 2.dp
                                                )
                                            } else {
                                                Text(
                                                    text = "Test",
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                    
                                    // Show/Hide Password Button
                                    IconButton(onClick = { showApiKey = !showApiKey }) {
                                        Icon(
                                            if (showApiKey) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle API key visibility"
                                        )
                                    }
                                }
                            },
                            isError = apiKey.isNotBlank() && apiKeyValidation != ApiKeyValidation.VALID
                        )
                        
                        // API Key Validation Messages
                        if (apiKey.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            when (apiKeyValidation) {
                                ApiKeyValidation.INVALID_FORMAT -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Error,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "API key should start with 'sk-'",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                ApiKeyValidation.TOO_SHORT -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = Color(0xFFFF9800),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "API key seems too short",
                                            fontSize = 12.sp,
                                            color = Color(0xFFFF9800)
                                        )
                                    }
                                }
                                ApiKeyValidation.CONTAINS_SPACES -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Error,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "API key should not contain spaces",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                ApiKeyValidation.VALID -> {
                                    when (apiKeyTestResult) {
                                        ApiKeyTestResult.SUCCESS -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color(0xFF4CAF50),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "API key verified successfully! ✨",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF4CAF50)
                                                )
                                            }
                                        }
                                        ApiKeyTestResult.INVALID_KEY -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.Error,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Invalid API key - check your key",
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                        ApiKeyTestResult.RATE_LIMITED -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.Warning,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFF9800),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Rate limit exceeded - try again later",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFFFF9800)
                                                )
                                            }
                                        }
                                        ApiKeyTestResult.INSUFFICIENT_PERMISSIONS -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.Error,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Insufficient permissions",
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                        ApiKeyTestResult.NO_INTERNET -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.Warning,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFF9800),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "No internet connection",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFFFF9800)
                                                )
                                            }
                                        }
                                        ApiKeyTestResult.TIMEOUT -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.Warning,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFF9800),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Request timeout",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFFFF9800)
                                                )
                                            }
                                        }
                                        is ApiKeyTestResult.API_ERROR -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.Error,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "API error: ${apiKeyTestResult.message}",
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                        null -> {
                                            if (!isTestingApiKey) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        Icons.Default.CheckCircle,
                                                        contentDescription = null,
                                                        tint = Color(0xFF4CAF50),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Valid format - click Test to verify",
                                                        fontSize = 12.sp,
                                                        color = Color(0xFF4CAF50)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Cost: ~$0.0007 per story with GPT-4o Mini • Leave blank for template stories",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Using enhanced template story generation",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Create Story Button
        Button(
            onClick = onCreateStoryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = childNames.any { it.isNotBlank() } && 
                     (selectedCharacters.isNotEmpty() || customCharacters.isNotBlank())
        ) {
            Text(
                text = "Create Story",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
    
    // Help Dialog
    if (showHelpDialog) {
        ApiKeyHelpDialog(
            onDismiss = { showHelpDialog = false }
        )
    }
}
