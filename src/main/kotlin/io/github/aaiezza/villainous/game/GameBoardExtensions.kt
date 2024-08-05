package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.Board
import io.github.aaiezza.villainous.Power
import io.github.aaiezza.villainous.VillainCard
import kotlin.math.min

fun Board.State.addPower(powerToAdd: Power): Board.State =
    this.copy(powerTokens = this.powerTokens + powerToAdd)

operator fun Power.plus(powerToAdd: Power) = Power(this.value + powerToAdd.value)
operator fun Power.unaryPlus() = Power(this.value + 1)
operator fun Power.minus(powerToRemove: Power) = Power(this.value - powerToRemove.value)
fun Power.minusDownToZero(powerToRemove: Power) = Power(maxOf(0, this.value - powerToRemove.value))

/**
 * This extension function will draw from the Villain card deck into the player's hand the number of cards specified,
 * <b>up to<b> the number of cards specified, and if the deck is depleted before that can finish, will shuffle the discard pile
 * and continue drawing, unless the deck is exhausted of cards.
 */
fun Board.State.drawVillainCards(numberOfCards: UInt, handLimit: UInt = 4u): Board.State {
    if (hand.size >= handLimit.toInt()) return this

    val delta = min(handLimit.toInt() - hand.size, numberOfCards.toInt() - hand.size)

    val newBoard = if (villainDeck.size >= delta) {
        this
    } else {
        shuffleAndReplaceVillainCardDiscardPile()
    }

    val drawnCards = if (newBoard.villainDeck.size >= delta) {
        newBoard.villainDeck.subList(0, delta)
    } else {
        newBoard.villainDeck
    }

    return copy(
        villainDeck = VillainCard.Deck(newBoard.villainDeck.drop(delta)),
        hand = VillainCard.Hand(newBoard.hand + drawnCards),
        villainDiscardPile = VillainCard.DiscardPile(newBoard.villainDiscardPile)
    )
}

fun Board.State.shuffleAndReplaceVillainCardDiscardPile(): Board.State =
    this.copy(
        villainDeck = VillainCard.Deck(this.villainDeck + this.villainDiscardPile.shuffled()),
        villainDiscardPile = VillainCard.DiscardPile()
    )

fun main() {
    println(listOf(1, 2, 3).drop(5))
}
