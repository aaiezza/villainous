package io.github.aaiezza.villainous.game

import assertk.assertThat
import assertk.assertions.hasSize
import io.github.aaiezza.villainous.Realm
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameTest {
    private lateinit var subject: Game

    @BeforeEach
    fun setUp() {
        subject = Game(
            listOf(
                "Prince John".asVillainousPlayer("1".asUsername()),
                "Maleficent".asVillainousPlayer("2".asUsername()),
                "Prince John".asVillainousPlayer("3".asUsername()),
                "Maleficent".asVillainousPlayer("4".asUsername()),
            ), 2
        )
    }

    @Test
    fun `should work as expected`() {
        assertThat(subject.history).hasSize(1)

        println("Villainous Game")
        println()
        subject.history.forEachIndexed { i, state ->
            println(" ----- State $i -----")
            state.players.forEach { (player, villain) ->
                println("${player.username.value} - Villain: ${villain.villainCharacter.name.value} ${if (player is Game.Player.Active) " (Active)" else ""}")
                println("Objective: ${villain.villainCharacter.objective.value}")
                println("Board:\n")
                villain.realm.flatMap { it.actionSpaceSlots + " | " }
                    .filter { it is Realm.Location.ActionSpaceSlot.CoverableActionSpaceSlot || it is String }
                    .forEach {
                        when (it) {
                            is Realm.Location.ActionSpaceSlot.CoverableActionSpaceSlot -> print("${it.actionSpace.actionName} _ ")
                            is String -> print(it)
                        }
                    }
                println()
                villain.realm.flatMap { it.actionSpaceSlots + " | " }
                    .filter { it is Realm.Location.ActionSpaceSlot.NotCoverableActionSpaceSlot || it is String }
                    .forEach {
                        when (it) {
                            is Realm.Location.ActionSpaceSlot.NotCoverableActionSpaceSlot -> print("${it.actionSpace.actionName} _ ")
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

                println("\n    +++++++\n")
            }
            println(" ----- -------- -----")
        }
    }
}