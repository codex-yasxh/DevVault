package com.aditya.devvault.presentation.github

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.devvault.data.repository.GitHubRepository
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import io.ktor.client.plugins.ResponseException

class GitHubViewModel(
    private val repository: GitHubRepository,
    private val prefs: DataStore<Preferences>
): ViewModel(){

    companion object{
        val USERNAME_KEY = stringPreferencesKey("github_username")
    }
    private val _uiState = MutableStateFlow<GitHubUiState>(GitHubUiState.Empty)
    val uiState: StateFlow<GitHubUiState> = _uiState.asStateFlow()

    private fun loadSignal(username : String){
        viewModelScope.launch {
            _uiState.value = GitHubUiState.Loading

            val result = repository.getSignal(username)

            result.fold(
                onSuccess = { fetchResult ->
                    _uiState.value = GitHubUiState.Success(
                        data = fetchResult.signal,
                        isStale = fetchResult.isStale
                    )
                },

                onFailure = { exception ->

                    val message = when {
                        exception is SocketTimeoutException ->
                            "The request timed out."

                        exception is ClientRequestException ->
                            "GitHub user not found."

                        exception is ServerResponseException ->
                            "GitHub is currently unavailable."

                        exception.message?.contains("UnknownHost", ignoreCase = true) == true ||
                        exception.message?.contains("connect", ignoreCase = true) == true ->
                            "No internet connection."

                        else ->
                            "Something went wrong. Please try again."
                    }

                    _uiState.value = GitHubUiState.Error(message)
                }
            )
        }
    }



    init{
        viewModelScope.launch {
            val savedUsername = prefs.data.map { it[USERNAME_KEY] }.firstOrNull()
            if(savedUsername.isNullOrBlank()){
                _uiState.value = GitHubUiState.Empty
            } else {
                loadSignal(savedUsername)
            }
        }
    }

    fun onUsernameSubmitted(username: String) {
        if (username.isBlank() || username.contains(" ")) {
            // local validation
            return
        }
        viewModelScope.launch {
            prefs.edit { it[USERNAME_KEY] = username }
        }
        loadSignal(username)
    }

    fun onRefresh(){
        viewModelScope.launch {
            val savedUsername = prefs.data.map { it[USERNAME_KEY] }.firstOrNull()
            if (!savedUsername.isNullOrBlank()){
                loadSignal(savedUsername)
            }
        }
    }
}

