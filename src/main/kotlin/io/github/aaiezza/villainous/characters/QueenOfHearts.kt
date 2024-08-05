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

class QueenOfHeartsVillainCard {
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

typealias VillainCard_QueenOfHearts_CardGuard = QueenOfHeartsVillainCard.CardGuard


val QueenOfHeartsBoard = {
    Board(
        villainCharacter = VillainCharacter(
            VillainCharacter.Name("Queen of Hearts"),
            VillainCharacter.Objective(
                "Have a Wicket at each location and successfully take a shot."
            ),
            VillainousExpansion.THE_WORST_TAKES_IT_ALL
        ),
        realm = Realm(
            Location(
                name = Location.Name("Courtyard"),
                actionSpaceSlots = listOf(
                    DISCARD().coverable(),
                    MOVE_AN_ITEM_OR_ALLY().coverable(),
                    GAIN_POWER(2u).notCoverable(),
                    PLAY_CARD().notCoverable(),
                )
            ),
            Location(
                name = Location.Name("Hedge Maze"),
                actionSpaceSlots = listOf(
                    PLAY_CARD().coverable(),
                    ACTIVATE().coverable(),
                    GAIN_POWER(3u).notCoverable(),
                    PLAY_CARD().notCoverable(),
                )
            ),
            Location(
                name = Location.Name("Tulgey Wood"),
                actionSpaceSlots = listOf(
                    FATE().coverable(),
                    PLAY_CARD().coverable(),
                    DISCARD().notCoverable(),
                    VANQUISH().notCoverable(),
                )
            ),
            Location(
                name = Location.Name("White Rabbit's House"),
                actionSpaceSlots = listOf(
                    PLAY_CARD().coverable(),
                    GAIN_POWER(1u).coverable(),
                    ACTIVATE().notCoverable(),
                    FATE().notCoverable(),
                )
            ),
        ),
        getVillainDeck = QUEEN_OF_HEARTS_VILLAIN_DECK,
        getFateDeck = QUEEN_OF_HEARTS_FATE_DECK
    )
}

val QUEEN_OF_HEARTS_VILLAIN_DECK = {
    listOf(
        {
            VillainCard_QueenOfHearts_CardGuard(
                Name("Card Guard: Club"),
                cost = CardCost(1),
                Strength(2)
            )
        } to 2,
        {
            VillainCard_QueenOfHearts_CardGuard(
                Name("Card Guard: Diamond"),
                cost = CardCost(1),
                Strength(2)
            )
        } to 2,
        {
            VillainCard_QueenOfHearts_CardGuard(
                Name("Card Guard: Heart"),
                cost = CardCost(2),
                Strength(3)
            )
        } to 2,
        {
            VillainCard_QueenOfHearts_CardGuard(
                Name("Card Guard: Spade"),
                cost = CardCost(2),
                Strength(3)
            )
        } to 2,
        {
            VillainCard.Standard.Effect(
                Name("Off With Your Head!"),
                Description("Defeat a Hero with a Strength of 4 or less."),
                cost = CardCost(3)
            )
        } to 3,
        {
            VillainCard.Standard.Item(
                Name("Spear"),
                Description(
                    "When Spear is played, attach it to an Ally. That Ally gets +1 Strength."
                ),
                cost = CardCost(1),
                VillainCard.Standard.Item.Effect.AddStrengthToAlly(Strength(+1))
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Take the Shot"),
                Description(
                    "If there is a wicket at each location, reveal the top five cards of your deck." +
                            "If the total Cost is less than the total Strength of all your Wickets, " +
                            "you make the shot and win the game. If not, discard the five revealed cards."
                ),
                cost = CardCost(4)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("By Order of the Queen"),
                Description(
                    "Convert up to two Card Guards to Wickets."
                ),
                cost = CardCost(2)
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Fury"),
                Description(
                    "During their turn, if another player defeats a Hero with a Strength of 4 or more, " +
                            "you may play Fury. Shrink up to two Heros."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Judgment"),
                Description(
                    "During their turn, if another player has three or more Allies in their Realm, " +
                            "you may play judgment. Gain 3 Power."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Effect(
                Name("Makes You Smaller"),
                Description(
                    "Either Shrink a Hero or turn an Enlarged Hero back to normal."
                ),
                cost = CardCost(2)
            )
        } to 2,
        {
            VillainCard.Standard.Effect(
                Name("Very Merry Unbirthday"),
                Description(
                    "Gain 1 Power for each Ally in your Realm."
                ),
                cost = CardCost(0)
            )
        } to 2,
        {
            VillainCard.Standard.Item(
                Name("Stopwatch"),
                Description(
                    "Gain 1 Power for each Wicket in your Realm."
                ),
                cost = CardCost(1),
                actionSpaceSlots = listOf(ACTIVATE().notCoverable())
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("The King"),
                Description(
                    "The Cost to play Card Guards is reduced by 1 Power."
                ),
                cost = CardCost(2),
                Strength(2)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Tweedle Dee & Tweedle Dum"),
                Description(
                    "Tweedle Dee and Tweedle Dum are not discarded when they are used to defeat a Hero."
                ),
                cost = CardCost(3),
                Strength(2)
            )
        } to 1,
    ).duplicateCards().let { VillainCard.Deck(it) }
}

val QUEEN_OF_HEARTS_FATE_DECK = {
    listOf(
        {
            FateCard.Standard.Effect(
                Name("I'm Late! I'm Late!"),
                Description(
                    "Choose and play a Hero with a Strength of 3 of less from Queen of Hearts' Fate discard pile."
                )
            )
        } to 2,
        {
            FateCard.Standard.Effect(
                Name("Makes you Larger"),
                Description(
                    "Either Enlarge a Hero or turn a Shrunken Hero back to normal."
                )
            )
        } to 2,
        {
            FateCard.Standard.Effect(
                Name("Mome Raths"),
                Description(
                    "Move an Ally to any location."
                )
            )
        } to 2,
        {
            FateCard.Standard.Hero(
                Name("Alice"),
                Description(
                    "Queen of Hearts cannot move Allies or Items."
                ),
                Strength(5)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Caterpillar"),
                Description(
                    "All Allies at Caterpillar's location get -1 Strength."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Cheshire Cat"),
                Description(
                    "When Cheshire Cat is played, you may convert up to two Wickets to Card Guards. " +
                            "When Cheshire Cat is defeated. " +
                            "Queen of Hearts may convert up to two Card Guards to Wickets."
                ),
                Strength(5)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Dodo"),
                Description(
                    "Card Guards at Dodo's location cannot be converted to Wickets."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Dormouse"),
                Description(
                    "Dormouse cannot be shrunk."
                ),
                Strength(1)
            )
        } to 1,
        {
            FateCard.Standard.Effect(
                Name("Down the Rabbit Hole"),
                Description(
                    "If Alice is in Queen of Hearts' Realm, discard an Ally from her location." +
                            "Otherwise, find Alice and play her."
                )
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Mad Hatter"),
                Description(
                    "Mad Hatter gets +2 Strength if March Hare is in Queen of Hearts' Realm."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("March Hare"),
                Description(
                    "March Hare gets +2 Strength if Mad Hatter is in Queen of Hearts' Realm."
                ),
                Strength(3)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("White Rabbit"),
                Description(
                    "The Cost to Activate Card Guards and Wickets is increased by 1 Power."
                ),
                Strength(2)
            )
        } to 1,
    ).duplicateCards().let { FateCard.Deck(it) }
}

class QueenOfHeartsSpecificBoardState : Board.State.VillainSpecific

class QueenOfHeartsBoardStateGenerator : CharacterBoardStateGenerator {
    override fun invoke(): Board.State = Board.State(QueenOfHeartsBoard()) { QueenOfHeartsSpecificBoardState() }
}

