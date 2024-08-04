package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.FateCard
import io.github.aaiezza.villainous.Power
import io.github.aaiezza.villainous.VillainCard
import io.github.aaiezza.villainous.game.Players.Companion.createPlayers

val GiveInitialPower = Game.State.Mover { gs ->
    gs.players.mapIndexedToPlayers { i, (player, board) ->
        player to board.copy(powerTokens = INITIAL_POWER_AMOUNTS.getValue(i).plus(board.powerTokens))
    }.let { gs.copy(players = it) }
}

val ShuffleAllVillainDecks = Game.State.Mover { gs ->
    gs.players.mapValues { (_, board) ->
        board.copy(villainDeck = board.villainDeck.shuffled().let { VillainCard.Deck(it) })
    }.let { gs.copy(players = createPlayers(it)) }
}

val ShuffleAllFateDecks = Game.State.Mover { gs ->
    gs.players.mapValues { (_, board) ->
        board.copy(fateDeck = board.fateDeck.shuffled().let { FateCard.Deck(it) })
    }.let { gs.copy(players = createPlayers(it)) }
}

val DealInitialHandsToAllPlayers = Game.State.Mover { gs ->
    gs.players.mapValues { (_, board) ->
        board.drawVillainCards(4u)
    }.let { gs.copy(players = createPlayers(it)) }
}


val INITIAL_POWER_AMOUNTS : Map<Int, Power> = mapOf(
    0 to Power(0),
    1 to Power(1),
    2 to Power(2),
    3 to Power(2),
    4 to Power(3),
    5 to Power(3),
).let { map ->
    map. withDefault {
        error(
            "Game does not currently support more than ${map.size} players, " +
                    "but the initial power amount for $it players was requested."
        )
    }
}
