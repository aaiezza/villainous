package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.Board
import io.github.aaiezza.villainous.game.Players.Companion.createPlayersFromList

data class Game private constructor(val history: List<State>) {
    init {
        require(history.isNotEmpty()) { "Nope!" }
    }

    val currentState: State
        get() = history.last()

    val startingPlayer: Player.State
        get() = history.first().playerStates.toList()[0]

    constructor(
        playerStates: List<Player.State.Inactive>,
        startingPlayerIndex: Int = 0
    ) : this(State(createPlayersFromList(playerStates, startingPlayerIndex)))

    private constructor(initialGameState: State) : this(listOf(initialGameState))

    fun progress(state: State): Game =
        Game(history + state)

    fun <R> mapIndexed(apply: (Int, Player.State) -> R) =
        currentState.playerStates.map { p -> currentState.playerStates.single { it.villainCharacterName == p.villainCharacterName } }
            .mapIndexed(apply)

    fun <R> map(apply: (Player.State) -> R) =
        currentState.playerStates.map { p -> currentState.playerStates.single { it.villainCharacterName == p.villainCharacterName } }
            .map(apply)

    data class Player(val username: Username) {
        data class Username(val value: String)

        interface State {
            val player: Player
            val boardState: Board.State

            val villainCharacterName get() = boardState.villainCharacter.name

            fun progress(apply: (Board.State) -> Board.State): State

            data class Active(override val player: Player, override val boardState: Board.State) :
                State {
                override fun progress(apply: (Board.State) -> Board.State) = copy(boardState = apply(boardState))
            }

            data class Inactive(override val player: Player, override val boardState: Board.State) :
                State {
                override fun progress(apply: (Board.State) -> Board.State) = copy(boardState = apply(boardState))
            }

            fun makeActive() = if (this !is Active) Active(player, boardState) else this
            fun makeInactive() = if (this !is Inactive) Inactive(player, boardState) else this
        }
    }

    data class State(val playerStates: List<Player.State>) {
        fun interface Mover {
            fun apply(game: Game): Game
        }
    }
}

fun List<Game.Player.State>.progressGame(game: Game) = game.progress(game.currentState.copy(playerStates = this))

data class Players private constructor(
    private val value: List<Game.Player.State>
) : List<Game.Player.State> by value {

    init {
        require(filterIsInstance<Game.Player.State.Active>().size == 1) { "Only one player can be active at a time" }
        require(first() is Game.Player.State.Active) { "First player must be the active player" }
    }

    companion object {
        private fun List<Game.Player.State>.markStartingPlayerAsActive(playerToMakeActive: Game.Player.State) =
            map { if (it == playerToMakeActive) it.makeActive() else it.makeInactive() }

        private fun List<Game.Player.State>.findActivePlayerIndex(): Int =
            mapIndexedNotNull { i, player -> if (player is Game.Player.State.Active) i else null }.single()

        private fun List<Game.Player.State>.shiftPlayersSoActiveIsFirst(): List<Game.Player.State> {
            val activePlayerIndex = this.findActivePlayerIndex()
            return ((activePlayerIndex..this.toList().indices.last) + (0 until activePlayerIndex)).map {
                this.toList()[it]
            }
        }

        fun createPlayersFromList(players: List<Game.Player.State>, startingPlayerIndex: Int) =
            createPlayers(players.markStartingPlayerAsActive(players[startingPlayerIndex]))

        private fun createPlayers(value: List<Game.Player.State>) = Players(value.shiftPlayersSoActiveIsFirst())
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
