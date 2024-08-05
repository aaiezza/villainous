package io.github.aaiezza.villainous.game.play.moves

import io.github.aaiezza.villainous.FateCard
import io.github.aaiezza.villainous.Power
import io.github.aaiezza.villainous.VillainCard
import io.github.aaiezza.villainous.game.Game
import io.github.aaiezza.villainous.game.Players.Companion.createPlayers
import io.github.aaiezza.villainous.game.drawVillainCards
import io.github.aaiezza.villainous.game.mapIndexedToPlayers
import io.github.aaiezza.villainous.game.plus

val ShuffleAllFateDecks = object : Game.State.Mover.AwaitingGame() {
    override fun apply(gameState: Game.State): Pair<Game.State, List<Game.State.Mover>> =
        gameState.players.mapValues { (_, board) ->
            board.copy(fateDeck = board.fateDeck.shuffled().let { FateCard.Deck(it) })
        }.let { Game.State(players = createPlayers(it)) } to listOf(
            ShuffleAllVillainDecks
        )
}

val ShuffleAllVillainDecks = object : Game.State.Mover.AwaitingGame() {
    override fun apply(gameState: Game.State): Pair<Game.State, List<Game.State.Mover>> =
        gameState.players.mapValues { (_, board) ->
            board.copy(villainDeck = board.villainDeck.shuffled().let { VillainCard.Deck(it) })
        }.let { Game.State(players = createPlayers(it)) } to listOf(
            DealInitialHandsToAllPlayers
        )
}

val DealInitialHandsToAllPlayers = object : Game.State.Mover.AwaitingGame() {
    override fun apply(gameState: Game.State): Pair<Game.State, List<Game.State.Mover>> =
        gameState.players.mapValues { (_, board) ->
            board.drawVillainCards(4u)
        }.let { Game.State(players = createPlayers(it)) } to listOf(GiveInitialPower)
}

val GiveInitialPower = object : Game.State.Mover.AwaitingGame() {
    override fun apply(gameState: Game.State): Pair<Game.State, List<Game.State.Mover>> {
        val newGameState = gameState.players.mapIndexedToPlayers { i, (player, board) ->
            player to board.copy(powerTokens = INITIAL_POWER_AMOUNTS.getValue(i).plus(board.powerTokens))
        }.let { Game.State(players = it) }

        return newGameState to VillainMoverMoveCalculator.calculateMoves(newGameState)
    }
}

val INITIAL_POWER_AMOUNTS: Map<Int, Power> = mapOf(
    0 to Power(0),
    1 to Power(1),
    2 to Power(2),
    3 to Power(2),
    4 to Power(3),
    5 to Power(3),
).let { map ->
    map.withDefault {
        error(
            "Game does not currently support more than ${map.size} players, " +
                    "but the initial power amount for $it players was requested."
        )
    }
}
