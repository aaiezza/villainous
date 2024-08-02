package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.VillainCharacter

fun String.asVillainousPlayer(username: Game.Player.Username) = Game.Player.Inactive(username, VillainCharacter.Name(this))
fun String.asUsername() = Game.Player.Username(this)