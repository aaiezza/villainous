package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.Board
import io.github.aaiezza.villainous.game.Players.Companion.createPlayersFromList


data class Game private constructor(val history: List<State>) {
    init {
        require(history.isNotEmpty()) { "Nope!" }
    }

    val currentState: State
        get() = history.last()

    val phase: Phase = currentState.phase

    val currentPlayerState: Player.State = currentState.playerStates.single { it is Player.State.Active }

    val startingPlayerState: Player.State
        get() = history.first().playerStates.toList()[0]

    constructor(
        playerStates: List<Player.State.Inactive>,
        startingPlayerIndex: Int = 0
    ) : this(State(createPlayersFromList(playerStates, startingPlayerIndex), phase = Phase.GamePhase.START_SET_UP))

    private constructor(initialGameState: State) : this(listOf(initialGameState))

    fun progressPhaseOnly(phase: Phase) = progress { it.copy(phase = phase) }

    fun progress(stateMover: (State) -> State): Game =
        Game(history + stateMover(currentState))

    fun mapIndexedPlayers(apply: (Int, Player.State) -> Player.State) =
        currentState.playerStates.mapIndexed(apply).let { Players(it) }

    fun mapPlayers(apply: (Player.State) -> Player.State) =
        currentState.playerStates.map(apply).let { Players(it) }

    data class State(val playerStates: Players, val phase: Phase)

    fun interface StateMover {
        fun apply(game: Game): Game
    }

    sealed interface Phase {
        enum class GamePhase : Phase {
            START_SET_UP,
            START_PLAYER_TURN,
            GAME_OVER;
        }

        enum class SetUpPhase : Phase {
            SHUFFLE_FATE_DECKS,
            SHUFFLE_VILLAIN_DECKS,
            DEAL_INITIAL_HANDS,
            GIVE_INITIAL_POWER;
        }

        enum class PlayerPhase : Phase {
            CHECK_PRE_TURN_WINNING_CONDITIONS,
            CHECK_CARD_PRECONDITIONS,
            MOVE_VILLAIN_MOVER,
            CHECK_MID_TURN_WINNING_CONDITIONS,
            TAKE_ACTIONS_OR_END_TAKE_ACTION_STEP,
            DRAW_CARDS,
            END_PLAYER_TURN;
        }

        enum class ActionPhase : Phase {
            REVEAL_HAND,
            REVEAL_VILLAIN_CARDS_FROM_DECK,
            REVEAL_FATE_CARDS_FROM_DECK;
        }
    }

    class Progresser {
        fun progressGame(game: Game): Game {
            return when (game.phase) {
                Phase.GamePhase.START_SET_UP -> SetupGame.apply(game)
                Phase.GamePhase.START_PLAYER_TURN -> TakePlayerTurn.apply(game)
                Phase.PlayerPhase.CHECK_PRE_TURN_WINNING_CONDITIONS -> TODO("")
                Phase.PlayerPhase.END_PLAYER_TURN ->
                    game.progress {
                        it.copy(
                            playerStates = game.currentState.playerStates.progressPlayers(),
                            phase = Phase.GamePhase.START_PLAYER_TURN
                        )
                    }

                Phase.GamePhase.GAME_OVER -> game
                else -> error("${game.phase} unknown")
            }
        }
    }

    interface Player : StateMover {
        data class Username(val value: String)

        val username: Username

        override fun apply(game: Game): Game

        fun duringAnotherPlayersTurn(): StateMover
        fun duringThisPlayersTurn(): StateMover

        interface State {
            val player: Player
            val boardState: Board.State

            val villainCharacterName get() = boardState.villainCharacter.name

            // TODO: Should this apply to Player States instead of Board.States?
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
}

fun Players.progressGame(game: Game, phase: Game.Phase = game.phase) =
    game.progress { it.copy(playerStates = this, phase = phase) }

data class Players internal constructor(
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
            return Players(((activePlayerIndex..this.toList().indices.last) + (0 until activePlayerIndex)).map {
                this.toList()[it]
            })
        }

        fun createPlayersFromList(players: List<Game.Player.State>, startingPlayerIndex: Int) =
            Players(createPlayers(players.markStartingPlayerAsActive(players[startingPlayerIndex])))

        private fun createPlayers(value: List<Game.Player.State>) = Players(value.shiftPlayersSoActiveIsFirst())
    }

    fun progressPlayers(): Players =
        Players(markStartingPlayerAsActive(mapIndexedNotNull { i, player -> if (i == 1) player else null }.single()).shiftPlayersSoActiveIsFirst())

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

abstract class AbstractPlayer : Game.Player {
    final override fun apply(game: Game): Game {
        val hiddenGame = game // TODO: Create a version of the game that the player would have access to
        // Remove all fate and villain decks with hidden cards
        // Remove all non-revealed hands
        // ---- Maaaaybe this becomes its own class
        return if (game.currentPlayerState.player == this) {
            duringThisPlayersTurn().apply(hiddenGame)
        } else {
            duringAnotherPlayersTurn().apply(hiddenGame)
        }
    }

    fun findMyState(game: Game): Game.Player.State =
        game.currentState.playerStates.single { it.player == this }
}
