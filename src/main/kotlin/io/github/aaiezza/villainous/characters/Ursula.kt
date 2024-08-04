package io.github.aaiezza.villainous.characters

import io.github.aaiezza.villainous.*
import io.github.aaiezza.villainous.ActionSpace.Standard.ACTIVATE
import io.github.aaiezza.villainous.ActionSpace.Standard.DISCARD
import io.github.aaiezza.villainous.ActionSpace.Standard.FATE
import io.github.aaiezza.villainous.ActionSpace.Standard.GAIN_POWER
import io.github.aaiezza.villainous.ActionSpace.Standard.MOVE_AN_ITEM_OR_ALLY
import io.github.aaiezza.villainous.ActionSpace.Standard.MOVE_A_HERO
import io.github.aaiezza.villainous.ActionSpace.Standard.PLAY_CARD
import io.github.aaiezza.villainous.Card.Description
import io.github.aaiezza.villainous.Card.Name
import io.github.aaiezza.villainous.Realm.Location

class UrsulaVillainCard {
    data class BindingContract(
        val locationName: Location.Name,
    ) : VillainCard.Standard.Item(
        Name("Binding Contract"),
        Description(
            "When Binding Contract is played, attach it to a Hero who is not at ${locationName.value}. " +
                    "That Hero is defeated when they are moved to ${locationName.value}"
        ),
        CardCost(2)
    )
}

typealias VillainCard_Ursula_BindingContract = UrsulaVillainCard.BindingContract

class UrsulaBoardGenerator : CharacterBoardGenerator {
    override fun invoke(): Board {
        return Board(
            villainCharacter = VillainCharacter(
                VillainCharacter.Name("Ursula"),
                VillainCharacter.Objective(
                    "Start your turn with the Trident and the Crown at Ursula's Lair."
                ),
                VillainousExpansion.THE_WORST_TAKES_IT_ALL
            ),
            realm = Realm(
                Location.Lockable.Unlocked(
                    name = Location.Name("Ursula's Lair"),
                    actionSpaceSlots = listOf(
                        GAIN_POWER(1u).coverable(),
                        ACTIVATE().coverable(),
                        MOVE_AN_ITEM_OR_ALLY().notCoverable(),
                        PLAY_CARD().notCoverable(),
                    )
                ),
                Location(
                    name = Location.Name("Eric's Ship"),
                    actionSpaceSlots = listOf(
                        GAIN_POWER(1u).coverable(),
                        PLAY_CARD().coverable(),
                        FATE().notCoverable(),
                        DISCARD().notCoverable(),
                    )
                ),
                Location(
                    name = Location.Name("The Shore"),
                    actionSpaceSlots = listOf(
                        PLAY_CARD().coverable(),
                        DISCARD().coverable(),
                        GAIN_POWER(3u).notCoverable(),
                        PLAY_CARD().notCoverable(),
                    )
                ),
                Location.Lockable.Locked(
                    name = Location.Name("The Palace"),
                    actionSpaceSlots = listOf(
                        MOVE_AN_ITEM_OR_ALLY().coverable(),
                        FATE().coverable(),
                        MOVE_A_HERO().notCoverable(),
                        GAIN_POWER(2u).notCoverable(),
                    )
                ),
            ),
            villainDeck = URSULA_VILLIAN_DECK(),
            fateDeck = URSULA_FATE_DECK()
        )
    }
}

val URSULA_VILLIAN_DECK = {
    listOf(
        {
            VillainCard_Ursula_BindingContract(
                Location.Name("Ursula's Lair"),
            )
        } to 1,
        {
            VillainCard_Ursula_BindingContract(
                Location.Name("Eric's Ship"),
            )
        } to 1,
        {
            VillainCard_Ursula_BindingContract(
                Location.Name("The Shore"),
            )
        } to 1,
        {
            VillainCard_Ursula_BindingContract(
                Location.Name("The Palace"),
            )
        } to 1,
        {
            VillainCard.Standard.Effect(
                Name("Change Form"),
                Description("Move the Lock Token to either Ursula's Lair or The Palace."),
                cost = CardCost(1)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Grow Giant"),
                Description(
                    "Perform one available action at a location adjacent to Ursula, " +
                            "even if the location is locked."
                ),
                cost = CardCost(1)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Opportunist"),
                Description(
                    "Choose an Item or Effect from your discard pile and put it into your hand."
                ),
                cost = CardCost(1)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Whirlpool"),
                Description(
                    "Move a Hero to any unlocked location."
                ),
                cost = CardCost(1)
            )
        } to 3,
        {
            VillainCard.Standard.Condition(
                Name("Arrogance"),
                Description(
                    "During their turn, if another player defeats a Hero with a Strength of 4 or more, " +
                            "you may play Arrogance. Draw three cards into your hand, then discard any three cards."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Effect(
                Name("Divination"),
                Description(
                    "Reveal cards from the top of your deck until you reveal a Binding Contract. " +
                            "Put that Binding Conract into your hand. Discard the rest."
                ),
                cost = CardCost(1)
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Trickery"),
                Description(
                    "During their turn, if another player has 6 or more Power, you may play Trickery. " +
                            "Reveal and play the top card of that player's Fate deck."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Item(
                Name("Cauldron"),
                Description(
                    "Gain 2 Power for each Binding Contract in your Realm."
                ),
                cost = CardCost(1),
                actionSpaceSlots = listOf(ACTIVATE().notCoverable())
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Name("Crown"),
                Description(
                    "Look at the top two cards of your Fate deck. Either discard both cards or " +
                            "return them to the top in any order."
                ),
                cost = CardCost(4),
                actionSpaceSlots = listOf(ACTIVATE().notCoverable())
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Flotsam"),
                Description(
                    "Move a Hero from Flotsam's location to an adjacent unlocked location."
                ),
                cost = CardCost(2),
                Strength(4)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Jetsam"),
                Description(
                    "Move a Hero from Jetsam's location to an adjacent unlocked location."
                ),
                cost = CardCost(2),
                Strength(4)
            )
        } to 1,
        {
            VillainCard.Standard.Effect(
                Name("Poor Unfortunate Souls"),
                Description(
                    "You may move each Hero to an adjacent unlocked location."
                ),
                cost = CardCost(2),
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Name("Trident"),
                Description(
                    "When Trident is played, find King Triton and play him to this location. " +
                            "Attach Trident to him. When King Triton is defeated, Trident is returend to Ursula " +
                            "at the same location."
                ),
                cost = CardCost(4),
            )
        } to 1,
    ).duplicateCards().let { VillainCard.Deck(it) }
}

val URSULA_FATE_DECK = {
    listOf(
        {
            FateCard.Standard.Effect(
                Name("Return to Form"),
                Description(
                    "Choose a Hero with a Strength of 4 or less from Ursula's Fate discard pile. " +
                            "Play that Hero to Ursula's location."
                )
            )
        } to 3,
        {
            FateCard.Standard.Item(
                Name("Dinglehopper"),
                Description(
                    "When Dinglehopper is played, attach it to a Hero. Each time Ursula moves to this location, " +
                            "she loses 1 Power."
                )
            )
        } to 2,
        {
            FateCard.Standard.Item(
                Name("Snarfblat"),
                Description(
                    "When Snarfblat is played, attach it to a Hero. " +
                            "The cost to play a Binding Contract on that Hero is increased by 3 Power."
                )
            )
        } to 2,
        {
            FateCard.Standard.Hero(
                Name("Ariel"),
                Description(
                    "When Ariel is played, you may move an unattached Item from an " +
                            "unlocked location to her location. Until Ariel is defeated, " +
                            "Ursula cannot perform the Move an Item or Ally action."
                ),
                Strength(4)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Eric"),
                Description(
                    "When Eric is played, you may move a Hero to any unlocked location."
                ),
                Strength(4)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Flounder"),
                Description(
                    "When Flounder is played, you may shuffle Ursula's discard pile into her deck."
                ),
                Strength(1)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Grimsby"),
                Description(
                    "When Grimsby is played, you may move the Lock Token to either Ursula's Lair or The Palace."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("King Triton"),
                Description(
                    "The Cost to play a Binding Contract or Effect " +
                            "that targets King Triton is increased by 1 Power."
                ),
                Strength(6)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Max"),
                Description(
                    "If Max is played to Ursula's location, you may move Ursula to any unlocked location."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Scuttle"),
                Description(
                    "When Scuttle is played, you may choose an Item from " +
                            "Ursula's Fate discard pile and attach it to Scuttle."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Sebastian"),
                Description(
                    "When Sebastian is played, you may choose a Binding Contract that is " +
                            "attached to a Hero at an unlocked location and attach it to Sebastian instead."
                ),
                Strength(2)
            )
        } to 1,
    ).duplicateCards().let { FateCard.Deck(it) }
}
