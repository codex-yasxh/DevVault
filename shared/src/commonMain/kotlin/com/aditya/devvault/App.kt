package com.aditya.devvault

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.aditya.devvault.presentation.home.HomeScreen
import com.aditya.devvault.presentation.home.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        HomeScreen(viewModel = koinViewModel())
    }
}
