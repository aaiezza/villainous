package io.github.aaiezza.villainous.game

import assertk.assertThat
import assertk.assertions.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameSetUpTest {
    private lateinit var subject: Game

    @BeforeEach
    fun setUp() {
        subject = Game(
            listOf(
                "Jafar".asVillainousPlayer("1".asUsername(), ::RandomPlayer),
                "Prince John".asVillainousPlayer("2".asUsername(), ::RandomPlayer),
                "King Candy".asVillainousPlayer("3".asUsername(), ::RandomPlayer),
                "Captain Hook".asVillainousPlayer("4".asUsername(), ::RandomPlayer),
                "Queen of Hearts".asVillainousPlayer("5".asUsername(), ::RandomPlayer),
                "Ursula".asVillainousPlayer("6".asUsername(), ::RandomPlayer),
            ), 1
        ).let { SetupGame.apply(it) }
    }

    @Test
    fun `should work as expected`() {
        assertThat(subject.history).hasSize(6)
        subject.print()
    }
}
