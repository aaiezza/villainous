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
                "Jafar".asVillainousPlayer("1".asUsername()),
                "Maleficent".asVillainousPlayer("2".asUsername()),
                "King Candy".asVillainousPlayer("3".asUsername()),
                "Captain Hook".asVillainousPlayer("4".asUsername()),
                "Queen of Hearts".asVillainousPlayer("5".asUsername()),
                "Ursula".asVillainousPlayer("6".asUsername()),
            ), 1
        ).let { SetupGame.apply(it) }
    }

    @Test
    fun `should work as expected`() {
        assertThat(subject.history).hasSize(5)

        println("Villainous Game")
        println()
        subject.history.forEachIndexed { i, state ->
            println(" ----- State $i -----")
            state.playerStates.forEach { playerState ->
                with(playerState) {
                    println("${player.username.value} - Villain: ${boardState.villainCharacter.name.value} ${if (playerState is Game.Player.State.Active) " (Active)" else ""}")
                    println("Exapansion: ${boardState.villainCharacter.villainousExpansion.value}")
                    println("Objective: ${boardState.villainCharacter.objective.value}")
                    println("Board:\n")
                    boardState.realm.flatMap { it.actionSpaceSlots + " | " }
                        .filter { it is Realm.Location.ActionSpaceSlot.CoverableActionSpaceSlot || it is String }
                        .forEach {
                            when (it) {
                                is Realm.Location.ActionSpaceSlot.CoverableActionSpaceSlot -> print("${it.actionSpace.actionName} _ ")
                                is String -> print(it)
                            }
                        }
                    println()
                    boardState.realm.flatMap { it.actionSpaceSlots + " | " }
                        .filter { it is Realm.Location.ActionSpaceSlot.NotCoverableActionSpaceSlot || it is String }
                        .forEach {
                            when (it) {
                                is Realm.Location.ActionSpaceSlot.NotCoverableActionSpaceSlot -> print("${it.actionSpace.actionName} _ ")
                                is String -> print(it)
                            }
                        }
                    println()
                    boardState.realm.forEach {
                        print(
                            "${it.name}" +
                                    when (it) {
                                        is Realm.Location.Lockable -> {
                                            if (it in boardState.lockedLocations) {
                                                " \uD83D\uDD12"
                                            } else " \uD83D\uDD13"
                                        }

                                        else -> " "
                                    } +
                                    "_ "
                        )
                    }
                    println()
                    println()

                    println("Power Tokens: ${boardState.powerTokens}")
                    println("Hand Size: ${boardState.hand.size}${if(boardState.hand.isNotEmpty()) " [${boardState.hand[0].name.value}]" else ""}")
                    println("Villain Card Deck: ${boardState.villainDeck.size}")
                    println("Villain Discard Pile: ${boardState.villainDiscardPile.size}")
                    println("Fate Card Deck: ${boardState.fateDeck.size}")
                    println("Fate Discard Pile: ${boardState.fateDiscardPile.size}")
                    println("Has Fate Token?: ${boardState.fateToken != null}")

                    println("\n    +++++++\n")
                }
                println(" ----- -------- -----")
            }
        }
    }
}
