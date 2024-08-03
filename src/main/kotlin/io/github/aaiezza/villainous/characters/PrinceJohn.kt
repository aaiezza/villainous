package io.github.aaiezza.villainous.characters

import io.github.aaiezza.villainous.*
import io.github.aaiezza.villainous.ActionSpace.Standard.DISCARD
import io.github.aaiezza.villainous.ActionSpace.Standard.FATE
import io.github.aaiezza.villainous.ActionSpace.Standard.GAIN_POWER
import io.github.aaiezza.villainous.ActionSpace.Standard.MOVE_AN_ITEM_OR_ALLY
import io.github.aaiezza.villainous.ActionSpace.Standard.PLAY_CARD
import io.github.aaiezza.villainous.ActionSpace.Standard.VANQUISH
import io.github.aaiezza.villainous.Card.Description
import io.github.aaiezza.villainous.Card.Name
import io.github.aaiezza.villainous.Realm.Location

class PrinceJohnBoardGenerator : CharacterBoardGenerator {
    override fun invoke(): Board {
        return Board(
            villainCharacter = VillainCharacter(
                VillainCharacter.Name("Prince John"),
                VillainCharacter.Objective("Start your turn with at least 20 power.")
            ),
            realm = Realm(
                Location(
                    name = Location.Name("Sherwood Forest"),
                    actionSpaceSlots = listOf(
                        GAIN_POWER(1u).coverable(),
                        DISCARD().coverable(),
                        PLAY_CARD().notCoverable(),
                        FATE().notCoverable(),
                    )
                ),
                Location(
                    name = Location.Name("Friar Tuck's Church"),
                    actionSpaceSlots = listOf(
                        GAIN_POWER(2u).coverable(),
                        PLAY_CARD().coverable(),
                        PLAY_CARD().notCoverable(),
                        MOVE_AN_ITEM_OR_ALLY().notCoverable(),
                    )
                ),
                Location(
                    name = Location.Name("Nottingham"),
                    actionSpaceSlots = listOf(
                        FATE().coverable(),
                        GAIN_POWER(1u).coverable(),
                        VANQUISH().notCoverable(),
                        PLAY_CARD().notCoverable(),
                    )
                ),
                Location(
                    name = Location.Name("The Jail"),
                    actionSpaceSlots = listOf(
                        GAIN_POWER(3u).notCoverable(),
                        PLAY_CARD().notCoverable(),
                        DISCARD().notCoverable(),
                    )
                ),
            ),
            villainDeck = PRINCE_JOHN_VILLIAN_DECK(),
            fateDeck = PRINCE_JOHN_FATE_DECK()
        )
    }
}

val PRINCE_JOHN_VILLIAN_DECK = {
    listOf(
        {
            VillainCard.Standard.Effect(
                Name("Beautiful, Lovely Taxes"),
                Description("Gain 1 Power for each Hero in your Realm."),
                cost = Power(0)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Imprison"),
                Description("Move a Hero to The Jail."),
                cost = Power(2)
            )
        } to 3,
        {
            VillainCard.Standard.Ally(
                Name("Rhino Guards"),
                Description("No additional Ability."),
                cost = Power(3),
                Strength(4)
            )
        } to 3,
        {
            VillainCard.Standard.Item(
                Name("Warrant"),
                Description("Gain 2 Power each time a Hero is played to this location."),
                cost = Power(3)
            )
        } to 3,
        {
            VillainCard.Standard.Ally(
                Name("Wolf Archers"),
                Description(
                    "When performing a Vanquish action, " +
                            "Wolf Archers may be used to defeat a Hero at their location or at an adjacent location."
                ),
                cost = Power(2),
                Strength(2)
            )
        } to 3,
        {
            VillainCard.Standard.Item(
                Name("Bow and Arrows"),
                Description(
                    "When Bow and Arrows is played, attach it to an Ally. " +
                            "That Ally gets +1 Strength. When that Ally would be discarded, discard this Item instead."
                ),
                cost = Power(1),
                VillainCard.Standard.Item.Effect.AddStrengthToAlly(Strength(+1))
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Cowardice"),
                Description(
                    "During their turn, if another player has three or more Allies in their Realm, " +
                            "you may play Cowardice. Play an Ally from your hand for free."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Greed"),
                Description(
                    "During their turn, if another player has 6 or more Power, " +
                            "you may play Greed. Gain 3 Power."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Effect(
                Name("Set a Trap"),
                Description(
                    "You may move an Ally to any location. Perform a Vanquish action."
                ),
                cost = Power(1)
            )
        } to 2,
        {
            VillainCard.Standard.Item(
                Name("Golden Arrow"),
                Description(
                    "When Golden Arrow is played, attach it to an Ally. " +
                            "When that Ally is used to defeat a Hero, gain 2 Power."
                ),
                cost = Power(0)
            )
        } to 1,
        {
            VillainCard.Standard.Effect(
                Name("Intimidation"),
                Description(
                    "Perform a Vanquish action, but do not discard the Allies used to defeat the Hero."
                ),
                cost = Power(2)
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Name("King Richard's Crown"),
                Description(
                    "If Prince John is at this location, all card Costs are reduced by 1 Power."
                ),
                cost = Power(1)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Nutsy"),
                Description(
                    "All other Allies at Nutsy's location get +1 Strength."
                ),
                cost = Power(2),
                Strength(2)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Sheriff of Nottingham"),
                Description(
                    "Before Prince John moves, you may move Sheriff of Nottingham " +
                            "to any location and gain 1 Power if there are any Heroes at his new location."
                ),
                cost = Power(3),
                Strength(3)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Sir Hiss"),
                Description(
                    "If Prince John is at Sir Hiss's location, you may perform one action that is covered by a Hero at that location."
                ),
                cost = Power(2),
                Strength(2)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Trigger"),
                Description(
                    "All other Allies at Trigger's location get -1 Strength."
                ),
                cost = Power(2),
                Strength(4)
            )
        } to 1,
    ).duplicateCards().let { VillainCard.Deck(it) }
}

val PRINCE_JOHN_FATE_DECK = {
    listOf(
        {
            FateCard.Standard.Item(
                Name("Clever Disguise"),
                Description(
                    "When Clever Disguise is played, attach it to a Hero. That Hero cannot be defeated. " +
                            "At any time, Prince Join may pay 2 Power to discard Clever Disguise."
                ),
            )
        } to 3,
        {
            FateCard.Standard.Effect(
                Name("Steal from the Rich"),
                Description(
                    "Take up to 4 Power from Prince John and put it on any one Hero. " +
                            "When that Hero is defeated, the Power is returned to Prince John."
                ),
            )
        } to 3,
        {
            FateCard.Standard.Hero(
                Name("Alan-A-Dale"),
                Description(
                    "All other Heroes in Prince John's Realm get +1 Strength."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Friar Tuck"),
                Description(
                    "When Friar Tuck is played, you may discard all Warrants from his location. " +
                            "Prince John does not gain any Power from them."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("King Richard"),
                Description(
                    "Prince John cannot play Effects."
                ),
                Strength(5)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Lady Kluck"),
                Description(
                    "Lady Kluck cannot be played or moved to The Jail."
                ),
                Strength(6)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Little John"),
                Description(
                    "When Little John is played, you may take up to 4 Power from Prince John " +
                            "and put it on Little John. When Little John is defeated, the Power is returned to Prince John."
                ),
                Strength(5)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Maid Marian"),
                Description(
                    "When Maid Marian is defeated, find Robin Hood and play him to the same location."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Robin Hood"),
                Description(
                    "The amount of Power that Prince John gains from each card or action is reduced by 1 Power."
                ),
                Strength(5)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Skippy"),
                Description(
                    "Wolf Archers cannot be used to defeat Skippy."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Toby"),
                Description(
                    "When Toby is defeated, shuffle him back into Prince John's Fate deck."
                ),
                Strength(2)
            )
        } to 1,
    ).duplicateCards().let { FateCard.Deck(it) }
}
