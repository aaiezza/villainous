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
import io.github.aaiezza.villainous.Realm.Location.Section.Companion.buildSections
import io.github.aaiezza.villainous.characters.KingCandyActionSpace.Companion.GAIN_POWER_INTERSECTION
import io.github.aaiezza.villainous.characters.KingCandyActionSpace.Companion.START_FINISH

class KingCandyVillainCard {
    open class CardGuard(
        name: Name,
        cost: Card.Cost,
        strength: Strength,
        attachments: List<Card.Placeable.ToCard> = emptyList(),
    ) : VillainCard.Standard.Ally(
        name,
        Description("Convert this Card Guard to a Wicket or back to a Card Guard."),
        cost,
        strength,
        attachments,
        actionSpaceSlots = listOf(ACTIVATE().withCost(1).notCoverable())
    ) {
        open fun toggleWicket(): CardGuard = Wicket(name, cost, strength, attachments)

        class Wicket(
            name: Name,
            cost: Card.Cost,
            unusableStrength: Strength,
            attachments: List<Card.Placeable.ToCard> = emptyList(),
        ) : CardGuard(name, cost, unusableStrength, attachments) {
            override fun toggleWicket() = CardGuard(name, cost, strength, attachments)
        }
    }
}

//typealias VillainCard_QueenOfHearts_CardGuard = QueenOfHeartsVillainCard.CardGuard

class KingCandyActionSpace {
    companion object {
        class StartFinish : ActionSpace.Standard.PlayCard()

        val START_FINISH = { StartFinish() }

        val GAIN_POWER_INTERSECTION = GAIN_POWER(3u).notCoverable()
    }
}

class KingCandyBoardGenerator : CharacterBoardGenerator {
    override fun invoke(): Board {
        return Board(
            villainCharacter = VillainCharacter(
                VillainCharacter.Name("King Candy"),
                VillainCharacter.Objective(
                    "Pass Start/Finish with a Glitch attached to Vanellope Von Schweetz."
                ),
                VillainousExpansion.SUGAR_AND_SPITE
            ),
            realm = Realm(
                Location(
                    name = Location.Name("Sugar Rush Speedway"),
                    actionSpaceSlots = listOf(
                        START_FINISH().notCoverable(),

                        FATE().coverable(Location.Section.Id(0u)),

                        DISCARD().coverable(Location.Section.Id(1u)),
                        PLAY_CARD().coverable(Location.Section.Id(1u)),

                        GAIN_POWER_INTERSECTION,

                        PLAY_CARD().notCoverable(),
                        DISCARD().notCoverable(),

                        MOVE_AN_ITEM_OR_ALLY().notCoverable(),
                        PLAY_CARD().notCoverable(),
                        GAIN_POWER(2u).notCoverable(),

                        FATE().coverable(Location.Section.Id(3u)),

                        DISCARD().coverable(Location.Section.Id(2u)),
                        PLAY_CARD().coverable(Location.Section.Id(2u)),

                        GAIN_POWER_INTERSECTION,

                        PLAY_CARD().notCoverable(),
                        FATE().notCoverable(),

                        VANQUISH().notCoverable(),
                        DISCARD().notCoverable(),
                    ),
                    sections = buildSections(4u)
                ),
            ),
            villainDeck = KING_CANDY_VILLIAN_DECK(),
            fateDeck = KING_CANDY_FATE_DECK()
        )
    }
}

val KING_CANDY_VILLIAN_DECK = {
    listOf(
        {
            VillainCard.Standard.Ally(
                Name("Candy Cy-Bug"),
                Description(
                    "Candy Cy-Bug is not discarded after use ina Vanquish action. " +
                            "Instead, increase its Strength by 1 and move it to a new location."
                ),
                cost = CardCost(2),
                Strength(2)
            )
        } to 3,
        {
            VillainCard.Standard.Item(
                Name("Glitch"),
                Description(
                    "When Glitch is played, attach it to Vanellope von Schweetz. " +
                            "If a Glitch is already attached, move King Candy and the Racer token " +
                            "forward two action spaces. If no other Glitch is attached, place the Racer token and " +
                            "King Candy's mover on Start/Finish. End your turn."
                ),
                cost = CardCost(1)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Go!"),
                Description(
                    "You may move up to two Allies to new locations."
                ),
                cost = CardCost(1)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Locked Up Memories"),
                Description(
                    "Either gain 3 Power or move the Racer token back two action spaces."
                ),
                cost = CardCost(1)
            )
        } to 3,
        {
            VillainCard.Standard.Ally(
                Name("Racers"),
                Description(
                    "When Racers is played or moved, gain 1 Power."
                ),
                cost = CardCost(1),
                Strength(1)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Turbo-Tastic"),
                Description(
                    "Play Turbo-Tastic without performing a Play a Card action. " +
                            "If any of your three available actions are covered, you may still take those actions."
                ),
                cost = CardCost(0)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Have Some Candy"),
                Description(
                    "Reveal the top five cards of your deck. Put up to two cards into your hand. Discard the rest."
                ),
                cost = CardCost(1)
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Out of Order"),
                Description(
                    "If another player gains 2 or more Power during their turn, you may play Out of Order. " +
                            "Choose any card in your discard pile and put it into your hand."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("The Most Powerful Virus in the Arcade"),
                Description(
                    "If another player moves an Ally or Item during their turn, you may play " +
                            "The Most Powerful Virus in the Arcade. Move forward two action spaces."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Effect(
                Name("Can't Be Allowed to Race"),
                Description(
                    "Move any number of Allies to a Hero's location. Perform a Vanquish action, " +
                            "but do not discard the Allies used to defeat the Hero. " +
                            "Then you may move the Racer token back three action spaces."
                ),
                cost = CardCost(6),
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Duncan and Wynnchel"),
                Description(
                    "When Duncan and Wynnchel is played or moved, you may perform a Vanquish action."
                ),
                cost = CardCost(2),
                Strength(3)
            )
        } to 1,
        {
            VillainCard.Standard.Effect(
                Name("Pay to Race"),
                Description(
                    "Before you perform actions on your turn, you may play Pay to Race without performing " +
                            "a Play a Card action. Pay up to 6 Power and move " +
                            "King Candy forward that number of action spaces."
                ),
                cost = Card.Cost.Variable()
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Name("Ralph's Hero Medal"),
                Description(
                    "When Ralph's Hero Medal is played, find Wreck-it Ralph and play him to this location. " +
                            "Attach Ralph's Hero Medal to him. When Wreck-It Ralph is defeated, " +
                            "find Vanellope von Schweetz and play her to this location."
                ),
                cost = CardCost(3)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Sour Bill"),
                Description(
                    "When Sour Bill is played or moved, " +
                            "you may reveal cards from the top of your deck until you find an Ally." +
                            "Put the Ally in your hand and place the remaining cards " +
                            "back on top of your deck in any order."
                ),
                cost = CardCost(1),
                Strength(1)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Taffyta Muttonfudge"),
                Description(
                    "When Taffyta Muttonfudge is played or moved, move the Racer token back two action spaces, " +
                            "or you may perform a Play a Card action."
                ),
                cost = CardCost(2),
                Strength(2)
            )
        } to 1,
    ).duplicateCards().let { VillainCard.Deck(it) }
}

val KING_CANDY_FATE_DECK = {
    listOf(
        {
            FateCard.Standard.Effect(
                Name("I Finally Have a Real Car!"),
                Description(
                    "Reveal the top card of King Candy's deck. You may move the Racer token forward the number " +
                            "of action spaces equal to that card's Cost, plus one. " +
                            "Put the card on the bottom of his deck."
                )
            )
        } to 2,
        {
            FateCard.Standard.Effect(
                Name("Just Glazed Me"),
                Description(
                    "King Candy must reveal his hand and discard two cards."
                )
            )
        } to 2,
        {
            FateCard.Standard.Effect(
                Name("Priness Vanellope"),
                Description(
                    "Move King Candy back, up to four action spaces."
                )
            )
        } to 2,
        {
            FateCard.Standard.Effect(
                Name("Unfinished Level"),
                Description(
                    "Look at the top four cards of King Candy's deck. Put two cards on the top and two " +
                            "on the bottom of his deck in any order."
                )
            )
        } to 2,
        {
            FateCard.Standard.Hero(
                Name("Fix-IT Felix Jr."),
                Description(
                    "When Fix-It Felix Jr. is played, you may move Vanellope von Schweetz to any location.\n" +
                            "At the start of King Candy's turn, he can only move forward two or three action spaces."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Sergeant Tamora Jean Calhoun"),
                Description(
                    "When Sergeant Tamora Jean Calhoun is played, " +
                            "you may move Vanellope von Schweetz to any location.\n" +
                            "King Candy must pay 1 Power to perform a Play a Card action."
                ),
                Strength(4)
            )
        } to 1,
        {
            FateCard.Standard.Effect(
                Name("The Beacon"),
                Description(
                    "Choose a location and move all adjacent Candy Cy-Bugs in play to that location. " +
                            "You may remove one Candy Cy-Bug from that location."
                )
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Vanellope von Schweetz"),
                Description(
                    "Before King Candy moves, if Vanellope von Schweetz has a Glitch attached, " +
                            "reveal the top card of King Candy's deck and move the Racer token forward " +
                            "that card's Cost plus two action spaces. Put the card on the bottom of his deck."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Effect(
                Name("Vanellope's Medal"),
                Description(
                    "You may choose and play a Hero from King Candy's discard pile. That Hero gains +1 Strength."
                )
            )
        } to 1,
        {
            FateCard.Standard.Effect(
                Name("What's With All the Magic Sparkles?"),
                Description(
                    "You may discard a Glitch. If you discard a Glitch and a Glitch is " +
                            "still attached to Vanellope von Schweetz, " +
                            "move the Racer token three action spaces forward."
                )
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Wreck-It Ralph"),
                Description(
                    "When Wreck-It Ralph is played, you may move Vanellope von Schweetz to any location.\n" +
                            "King Candy must pay 1 Power to perform a Move an ITem or Ally action."
                ),
                Strength(6)
            )
        } to 1,
    ).duplicateCards().let { FateCard.Deck(it) }
}
