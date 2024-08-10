package io.github.aaiezza.villainous.game

class RandomPlayer(override val username: Game.Player.Username) : AbstractPlayer() {
    override fun duringAnotherPlayersTurn(): Game.StateMover {
        // TODO: Check for condition card criteria in players hands,
        //  and prompt them to use it, or to use the card and not do the action if it is a "you may" action.
        return Game.StateMover { it }
    }

    override fun duringThisPlayersTurn(): Game.StateMover {
        // At this point, it is assumed that it is this player's turn.
        // We can check the phase, and see which step we are on.
        //     CHECK_PRE_TURN_WINNING_CONDITIONS
        // Check for preconditions or cards that apply prior to a turn
        // transition for this specific villain.
        //     MOVE_VILLAIN_MOVER
        // Ask player where they would like to move.
        //  (Check if it is valid? Maybe do this later to get things working for now.
        //  Can prevent cheating later. Example: moving to same location)
        //     CHECK_MID_TURN_WINNING_CONDITIONS
        // Check for winning conditions.
        // Game::progressGame should know how to deal with the GAME_OVER
        // state phase.
        //     TAKE_ACTIONS_OR_END_TAKE_ACTION_STEP (sub-phases)
        // Ask player to choose what action to take among their options.
        // Player should also perform it, and be responsible for moving
        //  the game through sub-phases associated with the task.
        // HOWEVER, I need to account for Checking other player's pre-conditions.
        // -- once all actions are exhausted, or the player has opted to end their turn,
        //  we enter into the DRAW_CARDS phase to end their turn
        //    DRAW_CARDS
        // Draw up to the number of cards in a player's hand limit.
        //    END_PLAYER_TURN
        // This is the phase we end on, that prompts the game to know that
        //  the game is not over and to move to make the next player ACTIVE,
        //  and this one INACTIVE.
        //   The alternative is to end with the GAME_OVER phase, and set the
        //   winning player somehow.



        return Game.StateMover {
            when(it.phase) {
                Game.Phase.PlayerPhase.MOVE_VILLAIN_MOVER -> {
                    val boardState = this.findMyState(it).boardState
                    val locationToMoveTo = boardState.realm.filter {  location ->
                        location !in boardState.lockedLocations && boardState.villainMoverLocation != location
                    }
                    // Choose from available locations
                        .first() // TODO: Just choose the first thing

                    it.mapPlayers { playerState ->
                        if(playerState.player != this)
                            playerState
                        else Game.Player.State.Active(this, boardState.copy(villainMoverLocation = locationToMoveTo))
                    }.progressGame(it, phase = Game.Phase.PlayerPhase.END_PLAYER_TURN)
                }
                else -> error("Unknown phase for player: ${it.phase}")
            }
        }
    }

}

class WinningPlayer(override val username: Game.Player.Username) : AbstractPlayer() {
    override fun duringAnotherPlayersTurn(): Game.StateMover {
        // TODO: Check for condition card criteria in players hands,
        //  and prompt them to use it, or to use the card and not do the action if it is a "you may" action.
        return Game.StateMover { it }
    }

    override fun duringThisPlayersTurn() = Game.StateMover { it.progressPhaseOnly(Game.Phase.GamePhase.GAME_OVER)
    }
}
