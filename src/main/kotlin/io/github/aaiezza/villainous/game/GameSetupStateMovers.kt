package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.Power

val DealInitialHand = Game.State.Mover { gameState ->
    gameState
}

val GiveInitialPower = Game.State.Mover { game ->
    game.copy(
        players =
        game.players.toList()
            // TODO: create a way to map over entries with the index
            // TODO: whenever starting to iterate over the players, always start with the Active one, and go on from there
            .filter {
                // TODO: remove this filter. Just for debugging
                println(it)
                true
            }
            .mapIndexed { i, (player, board) ->
            if (game.getActivePlayerBoard().player == player) {
                player to board.copy(powerTokens = INITIAL_POWER_AMOUNTS[i]?.plus(board.powerTokens) ?: error(""))
            } else player to board
        }.toMap()
    )
}

val INITIAL_POWER_AMOUNTS = mapOf(
    0 to Power(0),
    1 to Power(1),
    2 to Power(2),
    3 to Power(2),
    4 to Power(3),
    5 to Power(3),
)

