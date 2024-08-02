package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.Board
import io.github.aaiezza.villainous.VillainCharacter
import io.github.aaiezza.villainous.characters.getVillainBoard
import io.github.aaiezza.villainous.game.Players.Companion.createPlayersFromList


data class Game private constructor(val initialGameState: State) {
    val history = listOf(initialGameState)

    val currentState: State
        get() = history.last()

    val startingPlayer: PlayerAndBoard
        get() = history.first().players.toList()[0].let { PlayerAndBoard(it.first, it.second) }

    constructor (
        players: List<Player.Inactive>, startingPlayerIndex: Int = 0
    ) : this(State.initializeGameState(createPlayersFromList(players, startingPlayerIndex)))

    sealed interface Player {
        val username: Username
        val villainCharacterName: VillainCharacter.Name

        data class Username(val value: String)

        data class Active(override val username: Username, override val villainCharacterName: VillainCharacter.Name) :
            Player

        data class Inactive(override val username: Username, override val villainCharacterName: VillainCharacter.Name) :
            Player

        fun makeActive() = if (this !is Active) Active(username, villainCharacterName) else this
        fun makeInactive() = if (this !is Inactive) Inactive(username, villainCharacterName) else this
    }

    data class State private constructor(
        val players: Players
    ) {
        companion object {
            fun initializeGameState(players: Players) = State(players)
                .let(ShuffleAllFateDecks::apply)
                .let(ShuffleAllVillainDecks::apply)
                .let(DealInitialHandsToAllPlayers::apply)
                .let(GiveInitialPower::apply)
        }

        fun interface Mover {
            fun apply(gameState: State): State
        }
    }
}

class Players private constructor(
    private val value: Map<Game.Player, Board>
) : Map<Game.Player, Board> by value {
    init {
        require(filterKeys { it is Game.Player.Active }.size == 1) { "Only one player can be active at a time" }
        require(keys.first() is Game.Player.Active) { "First player must be the active player" }
    }

    companion object {
        private fun Map<Game.Player, Board>.markStartingPlayerAsActive(playerToMakeActive: Game.Player) =
            this.mapKeys { (player, _) -> if (player == playerToMakeActive) player.makeActive() else player.makeInactive() }

        private inline fun Map<Game.Player, Board>.findActivePlayerIndex(): Int =
            this.toList().mapIndexedNotNull { i, (player, _) -> if (player is Game.Player.Active) i else null }.single()

        private inline fun Map<Game.Player, Board>.shiftPlayersSoActiveIsFirst(): Map<Game.Player, Board> {
            val activePlayerIndex = this.findActivePlayerIndex()
            return ((activePlayerIndex..this.toList().indices.last) + (0 until activePlayerIndex)).associate {
                this.toList()[it]
            }
        }

        fun createPlayersFromList(players: List<Game.Player>, startingPlayerIndex: Int) =
            players.associateWith { getVillainBoard(it.villainCharacterName)() }
                .markStartingPlayerAsActive(players[startingPlayerIndex]).let { createPlayers(it) }

        fun createPlayers(value: Map<Game.Player, Board>) = Players(value.shiftPlayersSoActiveIsFirst())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Players) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return "Players(value=$value)"
    }
}

data class PlayerAndBoard(val player: Game.Player, val board: Board)
