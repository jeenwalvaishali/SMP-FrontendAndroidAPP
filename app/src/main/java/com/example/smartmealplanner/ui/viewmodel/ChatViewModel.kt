package com.example.smartmealplanner.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmealplanner.data.api.ApiService
import com.example.smartmealplanner.data.model.ChatMessage
import com.example.smartmealplanner.data.model.ChatRequest
import com.example.smartmealplanner.data.model.ChatResponse
import kotlinx.coroutines.launch

class ChatViewModel(private val apiService: ApiService) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        _messages.value = listOf(
            ChatMessage("Hi! How can I help with your meal plan?", false)
        )
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val currentMessages = _messages.value.orEmpty().toMutableList()
        currentMessages.add(ChatMessage(text, true))
        _messages.value = currentMessages

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: ChatResponse = apiService.sendMessage(ChatRequest(text))
                if (response.success) {
                    val updatedMessages = _messages.value.orEmpty().toMutableList()
                    updatedMessages.add(ChatMessage(response.answer, false))
                    _messages.value = updatedMessages
                } else {
                    _error.value = "Failed to get AI response"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
