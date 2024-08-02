package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.Board
import io.github.aaiezza.villainous.VillainCharacter
import io.github.aaiezza.villainous.characters.getVillainBoard

typealias Players = Map<Game.Player, Board>

data class Game private constructor(val initialGameState: State) {
    val history = listOf(initialGameState)

    val currentState: Game.State
        get() = history.last()

    constructor (
        players: List<Player.Inactive>, startingPlayerIndex: Int = 0
    ) : this(State.initializeGameState(players, startingPlayerIndex))

    sealed interface Player {
        val username: Username
        val villainCharacterName: VillainCharacter.Name

        data class Username(val value: String)

        data class Active(override val username: Username, override val villainCharacterName: VillainCharacter.Name) : Player
        data class Inactive(override val username: Username, override val villainCharacterName: VillainCharacter.Name) : Player

        fun makeActive() = if (this !is Active) Active(username, villainCharacterName) else this
        fun makeInactive() = if (this !is Inactive) Inactive(username, villainCharacterName) else this
    }

    data class State(
        val players: Players
    ) {
        companion object {
            private fun Players.markPlayerAsActive(playerToMakeActive: Player) =
                this.mapKeys { (player, _) -> if (player == playerToMakeActive) player.makeActive() else player.makeInactive() }

            fun initializeGameState(
                players: List<Player>, startingPlayerIndex: Int
            ) = State(players.associateWith { getVillainBoard(it.villainCharacterName)() }
                .markPlayerAsActive(players[startingPlayerIndex]))
                .let { GiveInitialPower.apply(it) }
        }

        fun interface Mover {
            fun apply(gameState: State): State
        }
    }
}

data class PlayerAndBoard(val player: Game.Player, val board: Board)
