package com.example.domain.entity

enum class Importance {
    Low, Basic, Important;

    override fun toString(): String {
        return when (this) {
            Low -> "low"
            Basic -> "basic"
            Important -> "important"
        }
    }

}
