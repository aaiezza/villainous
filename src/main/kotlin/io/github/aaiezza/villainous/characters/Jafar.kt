package io.github.aaiezza.villainous.characters

import io.github.aaiezza.villainous.*
import io.github.aaiezza.villainous.ActionSpace.Standard.ACTIVATE
import io.github.aaiezza.villainous.ActionSpace.Standard.DISCARD
import io.github.aaiezza.villainous.ActionSpace.Standard.FATE
import io.github.aaiezza.villainous.ActionSpace.Standard.GAIN_POWER
import io.github.aaiezza.villainous.ActionSpace.Standard.MOVE_AN_ITEM_OR_ALLY
import io.github.aaiezza.villainous.ActionSpace.Standard.PLAY_CARD
import io.github.aaiezza.villainous.ActionSpace.Standard.VANQUISH
import io.github.aaiezza.villainous.ActionSpace.Standard.withCost
import io.github.aaiezza.villainous.Card.Description
import io.github.aaiezza.villainous.Card.Name
import io.github.aaiezza.villainous.Realm.Location

class JafarBoardGenerator : CharacterBoardGenerator {
    override fun invoke(): Board {
        return Board(
            villainCharacter = VillainCharacter(
                VillainCharacter.Name("Jafar"),
                VillainCharacter.Objective(
                    "Start your turn with the Magic Lamp at Sultan's Palace " +
                            "and Genie under your control."
                ),
                VillainousExpansion.THE_WORST_TAKES_IT_ALL
            ),
            realm = Realm(
                Location(
                    name = Location.Name("Sultan's Palace"),
                    actionSpaceSlots = listOf(
                        PLAY_CARD().coverable(),
                        ACTIVATE().coverable(),
                        VANQUISH().notCoverable(),
                        FATE().notCoverable(),
                    )
                ),
                Location(
                    name = Location.Name("Streets of Agrabah"),
                    actionSpaceSlots = listOf(
                        GAIN_POWER(1u).coverable(),
                        FATE().coverable(),
                        DISCARD().notCoverable(),
                        PLAY_CARD().notCoverable(),
                    )
                ),
                Location(
                    name = Location.Name("Oasis"),
                    actionSpaceSlots = listOf(
                        ACTIVATE().coverable(),
                        PLAY_CARD().coverable(),
                        GAIN_POWER(3u).notCoverable(),
                        PLAY_CARD().notCoverable(),
                    )
                ),
                Location.Lockable.Locked(
                    name = Location.Name("Cave of Wonders"),
                    actionSpaceSlots = listOf(
                        DISCARD().coverable(),
                        GAIN_POWER(2u).coverable(),
                        PLAY_CARD().notCoverable(),
                        MOVE_AN_ITEM_OR_ALLY().notCoverable(),
                    )
                ),
            ),
            villainDeck = JAFAR_VILLIAN_DECK(),
            fateDeck = JAFAR_FATE_DECK()
        )
    }
}

val JAFAR_VILLIAN_DECK = {
    listOf(
        {
            VillainCard.Standard.Effect(
                Name("Necessary Sacrifice"),
                Description("Discard any Ally or Item under your control and gain 3 Power."),
                cost = CardCost(0)
            )
        } to 3,
        {
            VillainCard.Standard.Ally(
                Name("Palace Guard"),
                Description("No additional Ability."),
                cost = CardCost(1),
                Strength(2)
            )
        } to 3,
        {
            VillainCard.Standard.Item(
                Name("Scimitar"),
                Description(
                    "When Scimitar is played attach it to an Ally. " +
                            "That Ally gets +1 Strength."
                ),
                cost = CardCost(0),
                VillainCard.Standard.Item.Effect.AddStrengthToAlly(Strength(+1))
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Scrying"),
                Description(
                    "Choose either Item or Ally. " +
                            "Reveal cards from the top of your deck until you reveal a card of the chosen type. " +
                            "Put that card into your hand. Discard the rest."
                ),
                cost = CardCost(1)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("A Snake, Am I?"),
                Description(
                    "Defeat a Hero with a Strength of 4 or less at Jafar's location."
                ),
                cost = CardCost(2)
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Deception"),
                Description(
                    "During their turn, if another player has two or more Items in their Realm, " +
                            "you may play Deception. Reveal and play the top card of that player's Fate deck."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Item(
                Name("Giant Hourglass"),
                Description(
                    "Heroes at this location get -2 Strength until the end of your turn."
                ),
                cost = CardCost(1),
                actionSpaceSlots = listOf(ACTIVATE().notCoverable())
            )
        } to 2,
        {
            VillainCard.Standard.Effect(
                Name("Hypnotize"),
                Description(
                    "Defeat a Hero and move them to the bottom of your Board." +
                            "That Hero is under your control and treated as an Ally with the same Strength." +
                            "Ignore their Ability. The Cost to play Hypnotize is equal to the Hero's Strength."
                ),
                cost = Card.Cost.Variable()
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Manipulation"),
                Description(
                    "During their turn, if another player has three or more Allies in their Realm, " +
                            "you may play Manipulation. Choose any card from your discard pile and put it into your hand."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Effect(
                Name("Sorcerous Power"),
                Description(
                    "You may move a Hero to any unlocked location. You may move an Ally to any unlocked location."
                ),
                cost = CardCost(2)
            )
        } to 2,
        {
            VillainCard.Standard.Ally(
                Name("Gazeem"),
                Description(
                    "When Gazeem is discarded from your Realm, " +
                            "you may choose an Item from you discard pile and put it into your hand."
                ),
                cost = CardCost(2),
                Strength(2)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Iago"),
                Description(
                    "Move Iago and one unattached Item at his location to an adjacent unlocked location."
                ),
                cost = CardCost(1),
                Strength(1),
                actionSpaceSlots = listOf(ACTIVATE().withCost(1).notCoverable())
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Name("Magic Lamp"),
                Description(
                    "Magic Lamp may only be played to the Cave of Wonders.\n" +
                            "When Magic Lamp is played, find Genie and play him to the Cave of Wonders."
                ),
                cost = CardCost(4),
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Razoul"),
                Description(
                    "The Cost to play Allies to Razoul's location is reduced by 1 Power"
                ),
                cost = CardCost(3),
                Strength(3)
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Name("Scarab Pendant"),
                Description(
                    "When Scarab Pendant is played, unlock the Cave of Wonders.\n" +
                            "At the end of each turn, draw until you have five cards in your hand."
                ),
                cost = CardCost(3)
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Name("Snake Staff"),
                Description(
                    "Put a Hypnotize that is in your discard pile into your hand."
                ),
                cost = CardCost(2),
                actionSpaceSlots = listOf(ACTIVATE().withCost(1).notCoverable())
            )
        } to 1,
    ).duplicateCards().let { VillainCard.Deck(it) }
}

val JAFAR_FATE_DECK = {
    listOf(
        {
            FateCard.Standard.Item(
                Name("Wish"),
                Description(
                    "When Wish is played, attach it to a Hero. Tha Hero gets +2 Strength."
                ),
                FateCard.Standard.Item.Effect.AddStrengthToHero(Strength(+2))
            )
        } to 3,
        {
            FateCard.Standard.Effect(
                Name("Crushing Blow"),
                Description(
                    "Discard an Ally with a Strength of 3 or less from Jafar's Realm."
                )
            )
        } to 2,
        {
            FateCard.Standard.Effect(
                Name("Narrow Escape"),
                Description(
                    "Choose and play a Hero from Jafar's Fate discard pile."
                )
            )
        } to 2,
        {
            FateCard.Standard.Hero(
                Name("Abu"),
                Description(
                    "When Abu is played, you may choose any Item at his location and attach it to him. " +
                            "Jafar cannot use the Item. When Abu is defeated, " +
                            "the Item is returned to Jafar at the same location."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Aladdin"),
                Description(
                    "When Aladdin is played, you may choose any Item at his location and attach it to him. " +
                            "Jafar cannot use the Item. When Aladdin is defeated, " +
                            "the Item is returned to Jafar at the same location."
                ),
                Strength(4)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Carpet"),
                Description(
                    "Jafar must defeat Carpet before defeating other Heroes."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Genie"),
                Description(
                    "Genie gets +2 Strength if Magic Lamp is at his location."
                ),
                Strength(6)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Princess Jasmine"),
                Description(
                    "When Jafar draws cards at the end of each turn, he draws one less card."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Rajah"),
                Description(
                    "Rajah gets +2 Strength if Princess Jasmine is in Jafar's Realm."
                ),
                Strength(4)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Sultan"),
                Description(
                    "Palace Guards cannot be used to defeat Sultan."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Effect(
                Name("Treachery"),
                Description(
                    "Jafar loses up to 2 Power."
                )
            )
        } to 1,
    ).duplicateCards().let { FateCard.Deck(it) }
}
