package com.example.presentation.di

interface PresentationComponentProvider {
    fun providePresentationComponent(): PresentationComponent.Factory
}