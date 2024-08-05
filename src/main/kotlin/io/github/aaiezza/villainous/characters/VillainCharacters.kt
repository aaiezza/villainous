package io.github.aaiezza.villainous.characters

import io.github.aaiezza.villainous.Board
import io.github.aaiezza.villainous.Card
import io.github.aaiezza.villainous.Realm
import io.github.aaiezza.villainous.VillainCharacter

typealias CharacterBoardStateGenerator = () -> Board.State

fun getInitialVillainBoardState(villainCharacterName: VillainCharacter.Name): CharacterBoardStateGenerator =
    when (villainCharacterName) {
        VillainCharacter.Name("Prince John") -> PrinceJohnBoardStateGenerator()
        VillainCharacter.Name("Maleficent") -> MaleficentBoardStateGenerator()
        VillainCharacter.Name("Captain Hook") -> CaptainHookBoardStateGenerator()
        VillainCharacter.Name("Jafar") -> JafarBoardStateGenerator()
        VillainCharacter.Name("Queen of Hearts") -> QueenOfHeartsBoardStateGenerator()
        VillainCharacter.Name("Ursula") -> UrsulaBoardStateGenerator()
        VillainCharacter.Name("King Candy") -> KingCandyBoardStateGenerator()
        else -> throw IllegalArgumentException("Villain Character `$villainCharacterName` not found.")
    }

fun <C : Card> List<Pair<() -> C, Int>>.duplicateCards(): List<C> = flatMap { (getCard, times) ->
    (0 until times).map { getCard() }
}

val LOCK_LAST_LOCATION : (Board) -> List<Realm.Location.Lockable> = { listOf(it.realm.filterIsInstance<Realm.Location.Lockable>().last()) }
