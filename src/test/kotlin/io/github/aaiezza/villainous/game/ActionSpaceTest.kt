package io.github.aaiezza.villainous.game

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isIn
import assertk.assertions.isNotIn
import assertk.assertions.isTrue
import io.github.aaiezza.villainous.ActionSpace.Standard.DISCARD
import io.github.aaiezza.villainous.ActionSpace.Standard.FATE
import io.github.aaiezza.villainous.ActionSpace.Standard.GAIN_POWER
import io.github.aaiezza.villainous.ActionSpace.Standard.MOVE_AN_ITEM_OR_ALLY
import io.github.aaiezza.villainous.ActionSpace.Standard.PLAY_CARD
import io.github.aaiezza.villainous.ActionSpace.Standard.VANQUISH
import io.github.aaiezza.villainous.Realm.Location.Section
import io.github.aaiezza.villainous.characters.KingCandyActionSpace.Companion.GAIN_POWER_INTERSECTION
import io.github.aaiezza.villainous.characters.KingCandyActionSpace.Companion.START_FINISH
import io.github.aaiezza.villainous.characters.KingCandyBoard
import io.github.aaiezza.villainous.coverable
import io.github.aaiezza.villainous.notCoverable
import kotlin.test.Test

class ActionSpaceTest {
    @Test
    fun `should work as expected`() {
        val board = KingCandyBoard()
        val actionSpaceSlots = board.realm.flatMap { it.actionSpaceSlots }

        val specialSlots = listOf(actionSpaceSlots[5], actionSpaceSlots[8])

        assertThat(PLAY_CARD().notCoverable()).isNotIn(specialSlots)
        assertThat(specialSlots).contains(actionSpaceSlots[5])
    }
}
