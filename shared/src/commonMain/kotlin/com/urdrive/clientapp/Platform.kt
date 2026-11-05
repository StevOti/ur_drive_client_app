package com.urdrive.clientapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform