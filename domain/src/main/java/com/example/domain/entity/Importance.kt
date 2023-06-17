package com.example.domain.entity

enum class Importance {
    Low, Normal, Urgent;

    override fun toString(): String {
        return when (this) {
            Low -> "Low"
            Normal -> "Normal"
            Urgent -> "Urgent"
        }
    }
}
