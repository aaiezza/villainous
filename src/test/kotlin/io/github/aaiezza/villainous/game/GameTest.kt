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
                "Captain Hook".asVillainousPlayer("3".asUsername()),
                "Jafar".asVillainousPlayer("4".asUsername()),
                "Queen of Hearts".asVillainousPlayer("5".asUsername()),
                "Ursula".asVillainousPlayer("6".asUsername()),
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
                println("Exapansion: ${villain.villainCharacter.villainousExpansion.value}")
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
                villain.realm.forEach {
                    print(
                        "${it.name}" +
                                when (it) {
                                    is Realm.Location.Lockable.Locked -> " \uD83D\uDD12"
                                    is Realm.Location.Lockable.Unlocked -> " \uD83D\uDD13"
                                    else -> " "
                                } +
                                "_ "
                    )
                }
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
