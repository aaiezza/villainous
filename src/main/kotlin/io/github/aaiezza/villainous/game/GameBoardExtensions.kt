package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.Board
import io.github.aaiezza.villainous.Power

fun Board.addPower(powerToAdd: Power): Board =
    this.copy(powerTokens = this.powerTokens + powerToAdd)

operator fun Power.plus(powerToAdd: Power) = Power(this.value + powerToAdd.value)
operator fun Power.unaryPlus() = Power(this.value + 1)
operator fun Power.minus(powerToRemove: Power) = Power(this.value - powerToRemove.value)
fun Power.minusDownToZero(powerToRemove: Power) = Power(maxOf(0, this.value - powerToRemove.value))

fun Game.State.getActivePlayerBoard(): PlayerAndBoard = this.players.toList().first { it.first is Game.Player.Active }
    .let { PlayerAndBoard(it.first, it.second) }
