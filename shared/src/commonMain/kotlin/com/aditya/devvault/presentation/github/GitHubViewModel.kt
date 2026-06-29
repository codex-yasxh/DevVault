package com.aditya.devvault.presentation.github

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.devvault.data.repository.GitHubRepository
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class GitHubViewModel(
    private val repository: GitHubRepository
): ViewModel(){
    private val _uiState = MutableStateFlow<GitHubUiState>(GitHubUiState.Empty)
    val uiState: StateFlow<GitHubUiState> = _uiState.asStateFlow()

    fun loadSignal(username : String){
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

                    val message = when (exception) {
                        is SocketTimeoutException ->
                            "The request timed out."

                        is UnknownHostException ->
                            "No internet connection."

                        is ClientRequestException ->
                            "GitHub user not found."

                        is ServerResponseException ->
                            "GitHub is currently unavailable."

                        else ->
                            "Something went wrong. Please try again."
                    }

                    _uiState.value = GitHubUiState.Error(message)
                }
            )
        }
    }
}