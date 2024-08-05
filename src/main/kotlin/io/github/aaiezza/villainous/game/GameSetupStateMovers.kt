package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.FateCard
import io.github.aaiezza.villainous.Power
import io.github.aaiezza.villainous.VillainCard

val SetupGame = Game.State.Mover {
    it
        .let(ShuffleAllFateDecks::apply)
        .let(ShuffleAllVillainDecks::apply)
        .let(DealInitialHandsToAllPlayers::apply)
        .let(GiveInitialPower::apply)
}

val GiveInitialPower = Game.State.Mover { game ->
    game.mapIndexed { i, playerState ->
        playerState.progress { it.copy(powerTokens = INITIAL_POWER_AMOUNTS.getValue(i).plus(it.powerTokens)) }
    }.progressGame(game)
}

val ShuffleAllVillainDecks = Game.State.Mover { game ->
    game.map { playerState ->
        playerState.progress { it.copy(villainDeck = it.villainDeck.shuffled().let(VillainCard::Deck)) }
    }.progressGame(game)
}

val ShuffleAllFateDecks = Game.State.Mover { game ->
    game.map { playerState ->
        playerState.progress { it.copy(fateDeck = it.fateDeck.shuffled().let(FateCard::Deck)) }
    }.progressGame(game)
}

val DealInitialHandsToAllPlayers = Game.State.Mover { game ->
    game.map { playerState ->
        playerState.progress { it.drawVillainCards(4u) }
    }.progressGame(game)
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
