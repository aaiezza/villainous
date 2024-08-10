package io.github.aaiezza.villainous.game

val TakePlayerTurn = Game.StateMover { game ->
    if (game.phase != Game.Phase.GamePhase.START_PLAYER_TURN && game.phase !is Game.Phase.PlayerPhase)
        error("This should NOT have been called. Don't know how to recover the game.")
    else game
        .let { it.progressPhaseOnly(Game.Phase.PlayerPhase.MOVE_VILLAIN_MOVER) }
        // Ask active player to do something
        .let { it.currentPlayerState.player.duringThisPlayersTurn().apply(it) }
}
