package io.github.aaiezza.villainous.characters

import io.github.aaiezza.villainous.Board
import io.github.aaiezza.villainous.VillainCharacter

typealias CharacterBoardGenerator = () -> Board

fun getVillainBoard(villainCharacterName: VillainCharacter.Name): CharacterBoardGenerator =
    when (villainCharacterName) {
        VillainCharacter.Name("Prince John") -> PrinceJohnBoardGenerator()
        VillainCharacter.Name("Maleficent") -> MaleficentBoardGenerator()
        else -> throw IllegalArgumentException("Villain Character `$villainCharacterName` not found.")
    }
