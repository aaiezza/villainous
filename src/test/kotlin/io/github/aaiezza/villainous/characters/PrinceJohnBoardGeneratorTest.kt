package io.github.aaiezza.villainous.characters

import assertk.assertThat
import assertk.assertions.hasSize
import io.github.aaiezza.villainous.Realm.Location.ActionSpaceSlot.CoverableActionSpaceSlot
import io.github.aaiezza.villainous.Realm.Location.ActionSpaceSlot.NotCoverableActionSpaceSlot
import kotlin.test.BeforeTest
import kotlin.test.Test

class PrinceJohnBoardGeneratorTest {
    private lateinit var subject: PrinceJohnBoardGenerator

    @BeforeTest
    fun setUp() {
        subject = PrinceJohnBoardGenerator()
    }

    @Test
    fun `should produce expected results`() {
        val villain = subject()
        assertThat(villain.realm).hasSize(4)
        val expectedLocationActionSpaceSizes = listOf(4, 4, 4, 3)
        villain.realm.forEachIndexed { i, location ->
            assertThat(location.actionSpaceSlots).hasSize(
                expectedLocationActionSpaceSizes[i]
            )
        }
        println("Villain: ${villain.villainCharacter.name.value}")
        println("Objective: ${villain.villainCharacter.objective.value}")
        println("Board:\n")
        villain.realm.flatMap { it.actionSpaceSlots + " | " }
            .filter { it is CoverableActionSpaceSlot || it is String }
            .forEach {
                when (it) {
                    is CoverableActionSpaceSlot -> print("${it.actionSpace.actionName} _ ")
                    is String -> print(it)
                }
            }
        println()
        villain.realm.flatMap { it.actionSpaceSlots + " | " }
            .filter { it is NotCoverableActionSpaceSlot || it is String }
            .forEach {
                when (it) {
                    is NotCoverableActionSpaceSlot -> print("${it.actionSpace.actionName} _ ")
                    is String -> print(it)
                }
            }
        println()
        villain.realm.forEach { print("${it.name} _ ") }
        println()
        println()

        println("Power Tokens: ${villain.powerTokens}")
        println("Hand Size: ${villain.hand.size}")
        println("Villain Card Deck: ${villain.villainDeck.size}")
        println("Villain Discard Pile: ${villain.villainDiscardPile.size}")
        println("Fate Card Deck: ${villain.fateDeck.size}")
        println("Fate Discard Pile: ${villain.fateDiscardPile.size}")
        println("Has Fate Token?: ${villain.fateToken != null}")
    }
}
