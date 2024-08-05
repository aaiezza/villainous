package io.github.aaiezza.villainous.characters

import io.github.aaiezza.villainous.*
import io.github.aaiezza.villainous.ActionSpace.Standard.DISCARD
import io.github.aaiezza.villainous.ActionSpace.Standard.FATE
import io.github.aaiezza.villainous.ActionSpace.Standard.GAIN_POWER
import io.github.aaiezza.villainous.ActionSpace.Standard.MOVE_AN_ITEM_OR_ALLY
import io.github.aaiezza.villainous.ActionSpace.Standard.MOVE_A_HERO
import io.github.aaiezza.villainous.ActionSpace.Standard.PLAY_CARD
import io.github.aaiezza.villainous.ActionSpace.Standard.VANQUISH
import io.github.aaiezza.villainous.Card.Description
import io.github.aaiezza.villainous.Card.Name
import io.github.aaiezza.villainous.Realm.Location

class CaptainHookBoardGenerator : CharacterBoardGenerator {
    override fun invoke(): Board {
        return Board(
            villainCharacter = VillainCharacter(
                VillainCharacter.Name("Captain Hook"),
                VillainCharacter.Objective("Defeat Peter Pan at the Jolly Roger."),
                VillainousExpansion.THE_WORST_TAKES_IT_ALL
            ),
            realm = CAPTAIN_HOOK_REALM,
            villainDeck = CAPTAIN_HOOK_VILLIAN_DECK(),
            fateDeck = CAPTAIN_HOOK_FATE_DECK(),
            villainMoverLocation = CAPTAIN_HOOK_REALM[0]
        )
    }
}

val CAPTAIN_HOOK_REALM = Realm(
    Location(
        name = Location.Name("Jolly Roger"),
        actionSpaceSlots = listOf(
            GAIN_POWER(1u).coverable(),
            DISCARD().coverable(),
            VANQUISH().notCoverable(),
            PLAY_CARD().notCoverable(),
        )
    ),
    Location(
        name = Location.Name("Skull Rock"),
        actionSpaceSlots = listOf(
            GAIN_POWER(1u).coverable(),
            PLAY_CARD().coverable(),
            FATE().notCoverable(),
            DISCARD().notCoverable(),
        )
    ),
    Location(
        name = Location.Name("Mermaid Lagoon"),
        actionSpaceSlots = listOf(
            PLAY_CARD().coverable(),
            MOVE_AN_ITEM_OR_ALLY().coverable(),
            GAIN_POWER(3u).notCoverable(),
            PLAY_CARD().notCoverable(),
        )
    ),
    Location.Lockable.Locked(
        name = Location.Name("Hangman's Tree"),
        actionSpaceSlots = listOf(
            FATE().coverable(),
            GAIN_POWER(2u).coverable(),
            MOVE_A_HERO().notCoverable(),
            PLAY_CARD().notCoverable(),
        )
    ),
)

val CAPTAIN_HOOK_VILLIAN_DECK = {
    listOf(
        {
            VillainCard.Standard.Ally(
                Name("Boarding Party"),
                Description("When performing a Vanquish action, Boarding Party may be used to defeat a Hero at their location or at an adjacent unlocked location."),
                cost = CardCost(2),
                Strength(2)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Give Them a Scare"),
                Description("Look at the top two cards of your Fate deck. Either discard both cards or return them to the top in any order."),
                cost = CardCost(2)
            )
        } to 3,
        {
            VillainCard.Standard.Ally(
                Name("Swashbuckler"),
                Description("No additional Ability."),
                cost = CardCost(1),
                Strength(2)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Worthy Opponent"),
                Description("Gain 2 Power. Reveal cards from the top of your Fate deck until you were reveal a Hero. Play that Hero and discard the rest."),
                cost = CardCost(0)
            )
        } to 3,
        {
            VillainCard.Standard.Effect(
                Name("Aye, Aye, Sir!"),
                Description(
                    "Move an Ally to an adjacent unlocked location. That Ally gets +2 Strength until the end of your turn."
                ),
                cost = CardCost(1)
            )
        } to 2,
        {
            VillainCard.Standard.Item(
                Name("Cannon"),
                Description(
                    "This location gains:"
                ),
                cost = CardCost(2),
                actionSpaceSlots = listOf(VANQUISH().notCoverable())
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Cunning"),
                Description(
                    "During their turn, if another player has an Ally with a Strength or 4 or more in their Realm, " +
                            "you may play Cunning. Play an Ally from your hand for free."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Item(
                Name("Cutlass"),
                Description(
                    "When Cutlass is played, attach it to an Ally. That Ally gets +2 Strength."
                ),
                cost = CardCost(1),
                VillainCard.Standard.Item.Effect.AddStrengthToAlly(Strength(+2))
            )
        } to 2,
        {
            VillainCard.Standard.Item(
                Name("Hook's Case"),
                Description(
                    "This location gains:"
                ),
                cost = CardCost(2),
                actionSpaceSlots = listOf(GAIN_POWER(1u).notCoverable())
            )
        } to 2,
        {
            VillainCard.Standard.Condition(
                Name("Obsession"),
                Description(
                    "During their turn, if another player defeats a Hero with a Strength of 4 or more, " +
                            "you may play Obsession. " +
                            "Reveal cards from the top of your Fate deck until you reveal a Hero. " +
                            "Either play or discard that Hero. Discard the rest."
                )
            )
        } to 2,
        {
            VillainCard.Standard.Ally(
                Name("Pirate Brute"),
                Description(
                    "No additional Ability."
                ),
                cost = CardCost(3),
                Strength(4)
            )
        } to 2,
        {
            VillainCard.Standard.Item(
                Name("Ingenious Device"),
                Description(
                    "This location gains:"
                ),
                cost = CardCost(2),
                actionSpaceSlots = listOf(MOVE_A_HERO().notCoverable())
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Mr. Starkey"),
                Description(
                    "When Mr. Starkey is played, " +
                            "you may move a Hero from his location to an adjacent unlocked location."
                ),
                cost = CardCost(2),
                Strength(2)
            )
        } to 1,
        {
            VillainCard.Standard.Item(
                Name("Never Land Map"),
                Description(
                    "When Never Lan Map is played, unlock Hangman's Tree.\n" +
                            "When you play an Item, you may discard Never Land Map instead of paying the Item's Cost."
                ),
                cost = CardCost(4)
            )
        } to 1,
        {
            VillainCard.Standard.Ally(
                Name("Smee"),
                Description(
                    "Smee gets +2 Strength if he is at the Jolly Roger."
                ),
                cost = CardCost(2),
                Strength(2)
            )
        } to 1,
    ).duplicateCards().let { VillainCard.Deck(it) }
}

val CAPTAIN_HOOK_FATE_DECK = {
    listOf(
        {
            FateCard.Standard.Item(
                Name("Pixie Dust"),
                Description(
                    "When Pixie Dust is played, attach it to a Hero. That Hero gets +2 Strength."
                ),
                FateCard.Standard.Item.Effect.AddStrengthToHero(Strength(+2))
            )
        } to 3,
        {
            FateCard.Standard.Hero(
                Name("Lost Boys"),
                Description(
                    "When performing a Vanquish action to defeat Lost Boys, at least two Allies must be used."
                ),
                Strength(4)
            )
        } to 2,
        {
            FateCard.Standard.Effect(
                Name("Splitting Headache"),
                Description(
                    "Discard an Item from Captain Hook's Realm."
                )
            )
        } to 2,
        {
            FateCard.Standard.Item(
                Name("Taunt"),
                Description(
                    "When Taunt is played, attach it to a Hero. " +
                            "Captain Hook must defeat Heroes with Taunt before defeating other Heroes."
                )
            )
        } to 2,
        {
            FateCard.Standard.Hero(
                Name("John"),
                Description(
                    "John gets +1 Strength if he has any Items attached to him."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Michael"),
                Description(
                    "Michael gets +1 Strength for each location in Captain Hook's Realm that has a Hero, " +
                            "including Michael's location."
                ),
                Strength(1)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Peter Pan"),
                Description(
                    "When Peter Pan is revealed, you MUST IMMEDIATELY PLAY HIM to Hangman's Tree, " +
                            "even if it is locked. Any other Fate cards revealed during this action are discarded."
                ),
                Strength(8)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Tick Tock"),
                Description(
                    "If Captain Hook moves to Tick Tock's location, " +
                            "Captain Hook must immediately discard his hand."
                ),
                Strength(5)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Tinker Bell"),
                Description(
                    "When Tinker Bell is played, you may discard one Ally from her location."
                ),
                Strength(2)
            )
        } to 1,
        {
            FateCard.Standard.Hero(
                Name("Wendy"),
                Description(
                    "All other Heroes in Captain Hook's Realm get +1 Strength."
                ),
                Strength(2)
            )
        } to 1,
    ).duplicateCards().let { FateCard.Deck(it) }
}
