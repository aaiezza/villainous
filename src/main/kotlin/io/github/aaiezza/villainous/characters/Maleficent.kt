package io.github.aaiezza.villainous.characters

import io.github.aaiezza.villainous.*

class MaleficentVillainCard {
    data class Curse(
        override val name: Card.Name,
        override val description: Card.Description,
        override val cost: Power,
    ) : VillainCard, Card.WithCost, Card.Placeable.ToLocation
}

typealias VillainCard_Maleficent_Curse = MaleficentVillainCard.Curse


class MaleficentBoardGenerator : CharacterBoardGenerator {
    override fun invoke(): Board {
        return Board(
            villainCharacter = VillainCharacter(
                VillainCharacter.Name("Maleficent"),
                VillainCharacter.Objective("Start your turn with a Curse at each location.")
            ),
            realm = Realm(
                Realm.Location(
                    name = Realm.Location.Name("Forbidden Mountains"),
                    actionSpaceSlots = listOf(
                        ActionSpace.MOVE_AN_ITEM_OR_ALLY().coverable(),
                        ActionSpace.PLAY_CARD().coverable(),
                        ActionSpace.GAIN_POWER(1u).notCoverable(),
                        ActionSpace.FATE().notCoverable(),
                    )
                ),
                Realm.Location(
                    name = Realm.Location.Name("Briar Rose's Cottage"),
                    actionSpaceSlots = listOf(
                        ActionSpace.GAIN_POWER(2u).coverable(),
                        ActionSpace.MOVE_AN_ITEM_OR_ALLY().coverable(),
                        ActionSpace.PLAY_CARD().notCoverable(),
                        ActionSpace.DISCARD().notCoverable(),
                    )
                ),
                Realm.Location(
                    name = Realm.Location.Name("The Forest"),
                    actionSpaceSlots = listOf(
                        ActionSpace.DISCARD().coverable(),
                        ActionSpace.PLAY_CARD().coverable(),
                        ActionSpace.GAIN_POWER(3u).notCoverable(),
                        ActionSpace.PLAY_CARD().notCoverable(),
                    )
                ),
                Realm.Location(
                    name = Realm.Location.Name("King Stefan's Castle"),
                    actionSpaceSlots = listOf(
                        ActionSpace.GAIN_POWER(1u).coverable(),
                        ActionSpace.FATE().coverable(),
                        ActionSpace.VANQUISH().notCoverable(),
                        ActionSpace.PLAY_CARD().notCoverable(),
                    )
                ),
            ),
            villainDeck = MALEFICENT_VILLIAN_DECK(),
            fateDeck = MALEFICENT_FATE_DECK()
        )
    }
}

val MALEFICENT_VILLIAN_DECK = {
    listOf(
        {
            VillainCard.Standard.Ally(
                Card.Name("Cackling Good"),
                Card.Description("Cackling Goon gets +1 Strength for each Hero at his location."),
                Power(1),
                Strength(1)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Card.Name("Dragon Form"),
                Card.Description("Defeat a Hero with a Strength of 3 or less. If a Fate action targets you before your next turn, gain 3 Power."),
                Power(3)
            )
        } to 3,
        {
            VillainCard_Maleficent_Curse(
                Card.Name("Forest of thorns"),
                Card.Description("Heroes must have a Strength of 4 or more to be played to this location.\nDiscard this Curse when a Hero is played to this location."),
                cost = Power(2),
            )
        } to 3,
        {
            VillainCard_Maleficent_Curse(
                Card.Name("Green Fire"),
                Card.Description("Heroes cannot be played to this location.\nDiscard this Curse if Maleficent moves to this location."),
                cost = Power(3)
            )
        } to 3,
        {
            VillainCard.Standard.Ally(
                Card.Name("Savage Goon"),
                Card.Description(
                    "No additional Ability"
                ),
                cost = Power(3),
                Strength(4)
            )
        } to 3,
        {
            VillainCard.Standard.Ally(
                Card.Name("Sinister Goon"),
                Card.Description(
                    "Sinister Goon gets +1 Strength if there are any Curses at his location."
                ),
                cost = Power(2),
                Strength(3)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Card.Name("Vanish"),
                Card.Description(
                    "On your next turn, Maleficent does not have to move to a new location."
                ),
                cost = Power(0)
            )
        } to 3,
        {
            VillainCard_Maleficent_Curse(
                Card.Name("Dreamless Sleep"),
                Card.Description(
                    "Heroes at this location get -2 Strength.\n" +
                            "Discard this Curse when an Ally is played to this location."
                ),
                cost = Power(3)
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Card.Name("Malice"),
                Card.Description(
                    "During their turn, if another player defeats a Hero with a Strength of 4 or more, " +
                            "you may play Malice. Defeat a Hero with a Strength of 4 or less."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Card.Name("Tyranny"),
                Card.Description(
                    "During their turn, if another player has three or more Allies in their Realm, " +
                            "you may play Tyranny. Draw three cards into your hand, then discard any three cards."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Ally(
                Card.Name("Raven"),
                Card.Description(
                    "Before Maleficent moves, you may move Raven to any location and perform one " +
                            "available action at his new location. Raven cannot perform Fate actions."
                ),
                cost = Power(3),
                Strength(1)
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Card.Name("Spinning Wheel"),
                Card.Description(
                    "If a Hero is defeated at this location, gain Power equal to the Hero's Strength minus 1."
                ),
                cost = Power(1)
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Card.Name("Staff"),
                Card.Description(
                    "If Maleficient is at this location, " +
                            "the Cost to play an Effect or Curse is reduced by 1 Power."
                ),
                cost = Power(1)
            )
        } to 1,
    ).flatMap { (getCard, times) -> (0 until times).map { getCard() } }
        .let { VillainCard.Deck(it) }
}

val MALEFICENT_FATE_DECK = {
    listOf(
        {
            FateCard.Standard.Hero(
                Card.Name("Guards"),
                Card.Description(
                    "When performing a Vanquish action to defeat Guards, " +
                            "at least two Allies must be used."
                ),
                Strength(3)
            )
        } to 3,
        {
            FateCard.Standard.Item(
                Card.Name("Sword of Truth"),
                Card.Description(
                    "When Sword of Truth is played, attach it to a Hero with no other attached Items. " +
                            "That Hero gets +2 Strength. " +
                            "The Cost to play a Curse to this location is increased by 2 Power."
                ),
                FateCard.Standard.Item.Effect.AddStrengthToHero(Strength(1))
            )
        } to 3,
        {
            FateCard.Standard.Effect(
                Card.Name("Once Upon a Dream"),
                Card.Description(
                    "Discard a Curse from a location in Maleficent's Realm that has a Hero."
                )
            )
        } to 2,
        {
            FateCard.Standard.Hero(
                Card.Name("Aurora"),
                Card.Description(
                    "When Aurora is played, reveal the top card of Maleficent's Fate deck. " +
                            "If it is a Hero, play it. Otherwise, return it to the top of the deck."
                ),
                Strength(4)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Card.Name("Fauna"),
                Card.Description(
                    "When Fauna is played, you may discard Dreamless Sleep from her location."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Card.Name("Flora"),
                Card.Description(
                    "When Flora is played, Maleficent must reveal her hand.\n" +
                            "Until Flora is defeated, Maleficent must play with her hand revealed."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Card.Name("King Hubert"),
                Card.Description(
                    "When King Hubert is played, " +
                            "you may move one Ally from each adjacent location to his location."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Card.Name("King Stefan"),
                Card.Description(
                    "When King Stefan is played, you may move Maleficent to any location."
                ),
                Strength(4)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Card.Name("Merryweather"),
                Card.Description(
                    "Curses cannot be played to Merryweather's location."
                ),
                Strength(4)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Card.Name("Prince Phillip"),
                Card.Description(
                    "When Prince Phillip is played, you may discrad all Allies from his location."
                ),
                Strength(5)
            )
        } to 1,
    ).flatMap { (getCard, times) -> (0 until times).map { getCard() } }
        .let { FateCard.Deck(it) }
}
