package io.github.aaiezza.villainous.game.play

import io.github.aaiezza.villainous.game.Game

interface MoveCalculator {
    fun calculateMoves(gameState: Game.State): List<Game.State.Mover>
}
