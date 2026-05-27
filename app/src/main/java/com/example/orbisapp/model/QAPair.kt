package com.example.orbisapp.model

data class QAPair(
    val id: String,
    val question: String,
    val answer: String,
    val created_at: String? = null
)