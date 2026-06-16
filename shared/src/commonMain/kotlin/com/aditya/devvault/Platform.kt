package com.aditya.devvault

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform