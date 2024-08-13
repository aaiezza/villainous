package io.github.aaiezza.villainous.game

import io.github.aaiezza.villainous.Realm
import io.github.aaiezza.villainous.VillainCharacter
import io.github.aaiezza.villainous.characters.getInitialVillainBoardState

fun String.asVillainousPlayer(username: Game.Player.Username, playerType: (Game.Player.Username) -> Game.Player) =
    Game.Player.State.Inactive(playerType(username), getInitialVillainBoardState(VillainCharacter.Name(this))())

fun String.asUsername() = Game.Player.Username(this)

fun Game.print(): Unit {
    println()
    println("Villainous Game")
    println()
    this.history.forEachIndexed { i, state ->
        println(" ++++++ State $i ++++++ [${state.phase}]")
        state.playerStates.forEach { playerState ->
            with(playerState) {
                println("    ${player.username.value} - Villain: ${boardState.villainCharacter.name.value} ${if (playerState is Game.Player.State.Active) " (Active)" else ""}")
                println("    Expansion: ${boardState.villainCharacter.villainousExpansion.value}")
                println("    Objective: ${boardState.villainCharacter.objective.value}")
                println("\n    Board:")
                print("    ")
                boardState.realm.flatMap { it.actionSpaceSlots + " | " }
                    .filter { it is Realm.Location.ActionSpaceSlot.CoverableActionSpaceSlot || it is String }
                    .forEach {
                        val prefix = if (boardState.villainMoverLocation == it) "📍 " else ""
                        when (it) {
                            is Realm.Location.ActionSpaceSlot.CoverableActionSpaceSlot -> print("$prefix${it.actionSpace.actionName} _ ")
                            is String -> print("$prefix$it")
                        }
                    }
                println()
                print("    ")
                boardState.realm.flatMap { it.actionSpaceSlots + " | " }
                    .filter { it is Realm.Location.ActionSpaceSlot.NotCoverableActionSpaceSlot || it is String }
                    .forEach {
                        val prefix = if (boardState.villainMoverLocation == it) "📍 " else ""
                        when (it) {
                            is Realm.Location.ActionSpaceSlot.NotCoverableActionSpaceSlot -> print(
                                "$prefix${it.actionSpace.actionName} _ "
                            )

                            is String -> print("    $prefix$it")
                        }
                    }
                println()
                print("    ")
                boardState.realm.forEach {
                    val prefix = if (boardState.villainMoverLocation == it) "📍 " else ""
                    print("$prefix${it.name}")
                    print(
                        when (it) {
                            is Realm.Location.Lockable -> {
                                if (it in boardState.lockedLocations) {
                                    " \uD83D\uDD12"
                                } else " \uD83D\uDD13"
                            }
                            else -> " "
                        } + "_ "
                    )
                }
                println()
                println()

                print("    Power Tokens: ${boardState.powerTokens}")
                print("  ✋ Hand Size: ${boardState.hand.size}${if (boardState.hand.isNotEmpty()) " [${boardState.hand[0].name.value}]" else ""}")
                print("  Villain Card Deck: ${boardState.villainDeck.size}")
                println(" 🃟 Villain Discard Pile: ${boardState.villainDiscardPile.size}")
                print("    Fate Card Deck: ${boardState.fateDeck.size}")
                print("  🃟 Fate Discard Pile: ${boardState.fateDiscardPile.size}")
                println("  * Has Fate Token?: ${boardState.fateToken != null}")

                println("    +++++++\n")
            }
            println(" ----- -------- -----")
        }
    }
}
