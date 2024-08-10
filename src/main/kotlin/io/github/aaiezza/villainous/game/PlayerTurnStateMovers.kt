package io.github.aaiezza.villainous.game

val TakePlayerTurn = Game.StateMover { game ->
    if (game.phase != Game.Phase.GamePhase.START_PLAYER_TURN)
        error("This should not have been called. Don't know how to recover the game.")
    else game
        // Ask active player to do something
        .let { it.currentPlayerState.player.duringThisPlayersTurn().apply(game) }
}
