package dev.kobietka.flashcards.domain.models

data class CardList(val name: String,
                    val count: Int,
                    val endless: Boolean,
                    val typingAnswer: Boolean,
                    val randomOrder: Boolean)
