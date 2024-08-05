package io.github.aaiezza.villainous.game.play.moves

import io.github.aaiezza.villainous.Realm
import io.github.aaiezza.villainous.game.Game
import io.github.aaiezza.villainous.game.play.MoveCalculator

sealed class VillainMoverStateMover : Game.State.Mover.AwaitingPlayer() {
    class MoveToLocation(val location: Realm.Location) : VillainMoverStateMover() {
        override fun apply(gameState: Game.State): Pair<Game.State, List<Game.State.Mover>> {
            TODO("Not yet implemented")
        }
    }

    class MoveToActionSpaceSlot(val actionSpaceSlot: Realm.Location.ActionSpaceSlot) : VillainMoverStateMover() {
        override fun apply(gameState: Game.State): Pair<Game.State, List<Game.State.Mover>> {
            TODO("Not yet implemented")
        }
    }
}

object VillainMoverMoveCalculator : MoveCalculator {
    override fun calculateMoves(gameState: Game.State): List<Game.State.Mover> {
        val playerAndBoard = gameState.activePlayer

        return when (playerAndBoard.board.villainMoverType) {
            VillainMoverStateMover.MoveToLocation::class ->
                playerAndBoard.board.realm.filterNot { it is Realm.Location.Lockable.Locked }
                    .filterNot {
                        playerAndBoard.board.villainMoverLocation is Realm.Location
                                && playerAndBoard.board.villainMoverLocation == it
                    }.map { VillainMoverStateMover.MoveToLocation(it) }

            VillainMoverStateMover.MoveToActionSpaceSlot::class -> {
                playerAndBoard.board.realm
                    .flatMap { it.actionSpaceSlots }
                    .let { slots ->
                        ((slots.indexOf(playerAndBoard.board.villainMoverLocation) until slots.size) + (0 until slots.indexOf(
                            playerAndBoard.board.villainMoverLocation
                        ))).map { slots[it] }
                    }.let { slots -> (1..4).map { slots[it] } }
                    .map { VillainMoverStateMover.MoveToActionSpaceSlot(it) }
            }

            else -> error("Unknown Move `${gameState.availableMoves}`")
        }
    }
}
