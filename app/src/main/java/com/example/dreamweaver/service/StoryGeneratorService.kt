package com.example.dreamweaver.service

import com.example.dreamweaver.api.ChatMessage
import com.example.dreamweaver.api.OpenAIApiService
import com.example.dreamweaver.api.OpenAIRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class StoryGeneratorService {
    
    private var apiKey: String? = null
    
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC // Less verbose for production
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS) // Longer read timeout for story generation
        .build()
    
    private val apiService = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenAIApiService::class.java)
    
    fun setApiKey(key: String) {
        apiKey = key.trim()
    }
    
    /**
     * Validates OpenAI API key format
     */
    fun validateApiKey(key: String): ApiKeyValidation {
        val trimmedKey = key.trim()
        
        return when {
            trimmedKey.isBlank() -> ApiKeyValidation.EMPTY
            !trimmedKey.startsWith("sk-") -> ApiKeyValidation.INVALID_FORMAT
            trimmedKey.length < 20 -> ApiKeyValidation.TOO_SHORT
            trimmedKey.contains(" ") -> ApiKeyValidation.CONTAINS_SPACES
            else -> ApiKeyValidation.VALID
        }
    }
    
    /**
     * Tests the API key by making a minimal API call
     */
    suspend fun testApiKey(key: String): ApiKeyTestResult {
        return try {
            val testRequest = OpenAIRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    ChatMessage(role = "user", content = "Say 'test' only")
                ),
                maxTokens = 5,
                temperature = 0.0
            )
            
            val response = apiService.createChatCompletion(
                authorization = "Bearer ${key.trim()}",
                request = testRequest
            )
            
            ApiKeyTestResult.SUCCESS
            
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> ApiKeyTestResult.INVALID_KEY
                429 -> ApiKeyTestResult.RATE_LIMITED
                403 -> ApiKeyTestResult.INSUFFICIENT_PERMISSIONS
                else -> ApiKeyTestResult.API_ERROR("HTTP ${e.code()}: ${e.message()}")
            }
        } catch (e: UnknownHostException) {
            ApiKeyTestResult.NO_INTERNET
        } catch (e: SocketTimeoutException) {
            ApiKeyTestResult.TIMEOUT
        } catch (e: Exception) {
            ApiKeyTestResult.API_ERROR("Network error: ${e.message}")
        }
    }
    
    suspend fun generateStory(
        childNames: String,
        characters: String,
        mode: String,
        holiday: String? = null
    ): StoryResult {
        
        // If no API key is set, fall back to template stories
        if (apiKey.isNullOrBlank()) {
            val templateStory = generateTemplateStory(childNames, characters, mode, holiday)
            return StoryResult.Success(templateStory, isAiGenerated = false)
        }
        
        // Validate API key format before making request
        val validation = validateApiKey(apiKey!!)
        if (validation != ApiKeyValidation.VALID) {
            val templateStory = generateTemplateStory(childNames, characters, mode, holiday)
            return StoryResult.Success(templateStory, isAiGenerated = false, 
                warning = "Invalid API key format. Used template story instead.")
        }
        
        return try {
            val prompt = buildStoryPrompt(childNames, characters, mode, holiday)
            
            val request = OpenAIRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    ChatMessage(role = "user", content = prompt)
                ),
                maxTokens = 600, // Increased for longer stories
                temperature = 0.8
            )
            
            val response = apiService.createChatCompletion(
                authorization = "Bearer $apiKey",
                request = request
            )
            
            val story = response.choices.firstOrNull()?.message?.content?.trim()
            
            if (story.isNullOrBlank()) {
                val templateStory = generateTemplateStory(childNames, characters, mode, holiday)
                StoryResult.Success(templateStory, isAiGenerated = false, 
                    warning = "AI returned empty response. Used template story instead.")
            } else {
                StoryResult.Success(story, isAiGenerated = true)
            }
            
        } catch (e: HttpException) {
            val fallbackStory = generateTemplateStory(childNames, characters, mode, holiday)
            when (e.code()) {
                401 -> StoryResult.Success(fallbackStory, isAiGenerated = false, 
                    warning = "Invalid API key. Used template story instead.")
                429 -> StoryResult.Success(fallbackStory, isAiGenerated = false, 
                    warning = "Rate limit exceeded. Used template story instead.")
                403 -> StoryResult.Success(fallbackStory, isAiGenerated = false, 
                    warning = "API access denied. Used template story instead.")
                else -> StoryResult.Success(fallbackStory, isAiGenerated = false, 
                    warning = "API error. Used template story instead.")
            }
        } catch (e: UnknownHostException) {
            val fallbackStory = generateTemplateStory(childNames, characters, mode, holiday)
            StoryResult.Success(fallbackStory, isAiGenerated = false, 
                warning = "No internet connection. Used template story instead.")
        } catch (e: SocketTimeoutException) {
            val fallbackStory = generateTemplateStory(childNames, characters, mode, holiday)
            StoryResult.Success(fallbackStory, isAiGenerated = false, 
                warning = "Request timeout. Used template story instead.")
        } catch (e: Exception) {
            val fallbackStory = generateTemplateStory(childNames, characters, mode, holiday)
            StoryResult.Success(fallbackStory, isAiGenerated = false, 
                warning = "Unexpected error. Used template story instead.")
        }
    }
    
    private fun buildStoryPrompt(
        childNames: String,
        characters: String,
        mode: String,
        holiday: String?
    ): String {
        val basePrompt = when (mode.lowercase()) {
            "playful" -> {
                "Create a fun, energetic story for $childNames featuring $characters. " +
                "Make it playful, adventurous, and exciting with lots of action and laughs. " +
                "Perfect for daytime reading with about 3-4 paragraphs. Include dialogue and vivid descriptions."
            }
            "bath time" -> {
                "Create a gentle, water-themed story for $childNames featuring $characters. " +
                "Include bubbles, splashing, and cleanliness themes. Make it soothing but fun. " +
                "Perfect for bath time with about 3-4 paragraphs. Include sensory details about water and soap."
            }
            "bedtime" -> {
                "Create a calm, peaceful bedtime story for $childNames featuring $characters. " +
                "Include themes of friendship, dreams, and gentle adventures. " +
                "Make it soothing and perfect for bedtime with about 3-4 paragraphs. End on a peaceful, sleepy note."
            }
            "holiday" -> {
                val holidayTheme = if (!holiday.isNullOrBlank()) " celebrating $holiday" else ""
                "Create a festive holiday story for $childNames featuring $characters$holidayTheme. " +
                "Include holiday magic, traditions, and celebration themes. " +
                "Make it joyful and festive with about 3-4 paragraphs. Include holiday-specific details and warmth."
            }
            else -> {
                "Create an engaging story for $childNames featuring $characters. " +
                "Make it age-appropriate, entertaining, and heartwarming with about 3-4 paragraphs."
            }
        }
        
        val characterNote = if (characters.contains("Chip") && characters.contains("Milo")) {
            "\n\nNote: If the story includes Chip and Milo, they are Minecraft YouTuber characters who are brothers and love building and exploring in Minecraft."
        } else ""
        
        val guidelines = "\n\nGuidelines: Use simple language appropriate for children ages 3-10. Include dialogue. Make it engaging and imaginative. Ensure the story has a clear beginning, middle, and happy ending."
        
        return basePrompt + characterNote + guidelines
    }
    
    private fun generateTemplateStory(
        childNames: String,
        characters: String,
        mode: String,
        holiday: String?
    ): String {
        return when (mode.lowercase()) {
            "playful" -> generatePlayfulStory(childNames, characters)
            "bath time" -> generateBathTimeStory(childNames, characters)
            "bedtime" -> generateBedtimeStory(childNames, characters)
            "holiday" -> generateHolidayStory(childNames, characters, holiday ?: "holiday")
            else -> generatePlayfulStory(childNames, characters)
        }
    }
    
    private fun generatePlayfulStory(childNames: String, characters: String): String {
        return "Once upon a time, $childNames was playing outside when suddenly $characters appeared! " +
                "\"Want to go on an adventure?\" asked $characters with a big smile.\n\n" +
                "\"Yes!\" shouted $childNames excitedly. Together, they discovered a hidden playground " +
                "with slides that sparkled like rainbows and swings that could fly through the clouds. " +
                "They played games, told jokes, and had races around the magical equipment.\n\n" +
                "As the golden sun began to set, painting the sky in beautiful colors, $characters said, " +
                "\"This was the best day ever!\" $childNames agreed, feeling happy and tired from all the fun. " +
                "They promised to meet again tomorrow for another exciting adventure.\n\n" +
                "$childNames went home with a big smile, already dreaming about what amazing things " +
                "they would discover next time with their wonderful friend $characters!"
    }
    
    private fun generateBathTimeStory(childNames: String, characters: String): String {
        return "It was bath time, and $childNames was getting ready for a warm, bubbly bath. " +
                "Suddenly, $characters appeared with a magical bottle of rainbow soap!\n\n" +
                "\"This soap creates the most amazing bubbles,\" explained $characters, pouring some into the water. " +
                "Instantly, the bathroom filled with bubbles of every color - purple ones that smelled like lavender, " +
                "blue ones that sparkled like stars, and yellow ones that felt as soft as silk.\n\n" +
                "$childNames and $characters played bubble games, making bubble castles and watching them " +
                "float gently around the room. The magical soap made $childNames feel clean and refreshed, " +
                "and the warm water was perfectly cozy.\n\n" +
                "After the bath, wrapped in a fluffy towel, $childNames felt squeaky clean and ready for " +
                "sweet dreams. \"Thank you for making bath time so special!\" said $childNames, giving " +
                "$characters a big, clean hug."
    }
    
    private fun generateBedtimeStory(childNames: String, characters: String): String {
        return "As the first stars began to twinkle in the night sky, $childNames was getting ready for bed. " +
                "$characters came to visit with a very special gift - a magical dream pillow made of moonbeams and starlight.\n\n" +
                "\"This pillow will bring you the most wonderful dreams,\" whispered $characters softly. " +
                "\"Dreams filled with friendship, gentle adventures, and beautiful places.\" They tucked the pillow " +
                "under $childNames' head, and immediately it felt as soft as a cloud.\n\n" +
                "$characters sat beside the bed, telling quiet stories about peaceful meadows where friendly " +
                "animals danced under the moonlight, and magical gardens where flowers sang lullabies. " +
                "The gentle words made $childNames feel safe, loved, and wonderfully sleepy.\n\n" +
                "Soon, $childNames drifted off to the most peaceful sleep, with $characters watching over " +
                "their dreams like a guardian angel. All through the night, the magical pillow brought " +
                "beautiful dreams of joy, adventure, and friendship."
    }
    
    private fun generateHolidayStory(childNames: String, characters: String, holiday: String): String {
        return "It was a very special $holiday, and $childNames was bubbling with excitement! " +
                "$characters arrived with arms full of festive decorations and holiday magic.\n\n" +
                "\"Let's make this the most wonderful $holiday ever!\" exclaimed $characters. Together, " +
                "they decorated with sparkling lights, colorful ornaments, and magical holiday symbols. " +
                "The air filled with the sweet scents of holiday treats and the warm glow of celebration.\n\n" +
                "They prepared special $holiday activities, singing festive songs and sharing stories " +
                "about the meaning of this special day. $characters showed $childNames how holiday magic " +
                "comes from sharing joy, kindness, and love with others.\n\n" +
                "As the $holiday celebration came to an end, $childNames felt their heart full of warmth " +
                "and happiness. \"This was the most magical $holiday ever!\" they said, hugging $characters tightly. " +
                "The holiday spirit would stay in their heart all year long, reminding them of this " +
                "perfect day filled with love, friendship, and wonder."
    }
}

// Data classes for validation and results
enum class ApiKeyValidation {
    VALID,
    EMPTY,
    INVALID_FORMAT,
    TOO_SHORT,
    CONTAINS_SPACES
}

sealed class ApiKeyTestResult {
    object SUCCESS : ApiKeyTestResult()
    object INVALID_KEY : ApiKeyTestResult()
    object RATE_LIMITED : ApiKeyTestResult()
    object INSUFFICIENT_PERMISSIONS : ApiKeyTestResult()
    object NO_INTERNET : ApiKeyTestResult()
    object TIMEOUT : ApiKeyTestResult()
    data class API_ERROR(val message: String) : ApiKeyTestResult()
}

sealed class StoryResult {
    data class Success(
        val story: String, 
        val isAiGenerated: Boolean, 
        val warning: String? = null
    ) : StoryResult()
    data class Error(val message: String) : StoryResult()
}
