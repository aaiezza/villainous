package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.Power
import io.github.aaiezza.villainous.mapIndexed
import io.github.aaiezza.villainous.mapIndexedToPlayers

val DealInitialHand = Game.State.Mover { gameState ->


    gameState
}

val GiveInitialPower = Game.State.Mover { game ->
    game.copy(
        players =
        game.players
            .filter {
//                println(it)
                true
            }
            .mapIndexedToPlayers { i, (player, board) ->
                player to board.copy(powerTokens = INITIAL_POWER_AMOUNTS[i]?.plus(board.powerTokens) ?: error(""))
            }
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

