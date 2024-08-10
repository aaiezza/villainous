package io.github.aaiezza.villainous.game

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GamePlayTest {
    private lateinit var subject: Game

    private lateinit var gameProgresser: Game.Progresser

    @BeforeEach
    fun setUp() {
        subject = Game(
            listOf(
                "Jafar".asVillainousPlayer("1".asUsername(), ::RandomPlayer),
                "Prince John".asVillainousPlayer("2".asUsername(), ::RandomPlayer),
                "King Candy".asVillainousPlayer("3".asUsername(), ::WinningPlayer),
            ), 1
        )

        gameProgresser = Game.Progresser()
    }

    @Test
    fun `should play the game`() {
        assertThat(subject.phase).isEqualTo(Game.Phase.GamePhase.START_SET_UP)
        val endGame = generateSequence(0 to subject) { (i, it) ->
            println("Step $i : ${it.phase}, ${it.currentPlayerState.boardState.villainCharacter.name}")
            if (it.phase == Game.Phase.GamePhase.GAME_OVER) null
            else i + 1 to gameProgresser.progressGame(it)
        }.last().second
        assertThat(endGame.phase).isEqualTo(Game.Phase.GamePhase.GAME_OVER)

        endGame.print()
    }
}
