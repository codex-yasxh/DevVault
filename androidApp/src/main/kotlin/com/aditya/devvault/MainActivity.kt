package com.aditya.devvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aditya.devvault.data.repository.GitHubRepository
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val gitHubRepository: GitHubRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}
