package dev.kobietka.flashcards.domain.service

import dev.kobietka.flashcards.domain.models.CardList

class CardListService {

    fun changeListName(cardList: CardList, newName: String): CardList {
        return cardList.copy(name = newName)
    }

    fun changeEndless(cardList: CardList, newState: Boolean): CardList {
        return cardList.copy(endless = newState)
    }

    fun changeTypingAnswer(cardList: CardList, newState: Boolean): CardList {
        return cardList.copy(typingAnswer = newState)
    }

    fun changeRandomOrder(cardList: CardList, newState: Boolean): CardList {
        return cardList.copy(randomOrder = newState)
    }

}