package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.VillainCharacter
import io.github.aaiezza.villainous.characters.getInitialVillainBoardState

fun String.asVillainousPlayer(username: Game.Player.Username) =
    Game.Player.State.Inactive(Game.Player(username), getInitialVillainBoardState(VillainCharacter.Name(this))())

fun String.asUsername() = Game.Player.Username(this)
