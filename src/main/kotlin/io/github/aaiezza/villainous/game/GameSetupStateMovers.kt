package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.FateCard
import io.github.aaiezza.villainous.Power
import io.github.aaiezza.villainous.VillainCard

val SetupGame = Game.StateMover { game ->
    if (game.phase != Game.Phase.GamePhase.START_SET_UP)
        error("This should not have been called. Don't know how to recover the game.")
    else game
        .let { it.progressPhaseOnly(Game.Phase.SetUpPhase.SHUFFLE_FATE_DECKS) }
        .let(ShuffleAllFateDecks::apply)
        .let(ShuffleAllVillainDecks::apply)
        .let(DealInitialHandsToAllPlayers::apply)
        .let(GiveInitialPower::apply)
}

val ShuffleAllFateDecks = Game.StateMover { game ->
    game.mapPlayers { playerState ->
        playerState.progress { it.copy(fateDeck = it.fateDeck.shuffled().let(FateCard::Deck)) }
    }.progressGame(game, phase = Game.Phase.SetUpPhase.SHUFFLE_VILLAIN_DECKS)
}

val ShuffleAllVillainDecks = Game.StateMover { game ->
    game.mapPlayers { playerState ->
        playerState.progress { it.copy(villainDeck = it.villainDeck.shuffled().let(VillainCard::Deck)) }
    }.progressGame(game, phase = Game.Phase.SetUpPhase.DEAL_INITIAL_HANDS)
}

val DealInitialHandsToAllPlayers = Game.StateMover { game ->
    game.mapPlayers { playerState ->
        playerState.progress { it.drawVillainCards(4u) }
    }.progressGame(game, phase = Game.Phase.SetUpPhase.GIVE_INITIAL_POWER)
}

val GiveInitialPower = Game.StateMover { game ->
    game.mapIndexedPlayers { i, playerState ->
        playerState.progress { it.copy(powerTokens = INITIAL_POWER_AMOUNTS.getValue(i).plus(it.powerTokens)) }
    }.progressGame(game, phase = Game.Phase.GamePhase.START_PLAYER_TURN)
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
