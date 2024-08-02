package io.github.aaiezza.villainous

data class VillainousBoardState(
    val boards: List<Board>,
)

data class Board(
    val villainCharacter: VillainCharacter,
    val realm: Realm,

    val powerTokens: Power = Power(0),
    val hand: VillainCard.Hand = VillainCard.Hand(),

    val villainDeck: VillainCard.Deck = VillainCard.Deck(),
    val villainDiscardPile: VillainCard.DiscardPile = VillainCard.DiscardPile(),
    val fateDeck: FateCard.Deck = FateCard.Deck(),
    val fateDiscardPile: FateCard.DiscardPile = FateCard.DiscardPile(),
    val fateToken: FateToken? = null,
)

data class VillainCharacter(val name: Name, val objective: Objective) {
    data class Name(val value: String)
    data class Objective(val value: String)
}

interface Card {
    val name: Name
    val description: Description

    data class Name(val value: String)
    data class Description(val value: String)

    interface WithCost : Card {
        val cost: Power
    }

    interface WithStrength : Card {
        val strength: Strength
    }

    interface Movable : Card

    sealed interface Placeable : Movable {
        interface ToLocation : Placeable
        interface ToCard : Placeable
    }

    interface CanHaveAttachments : Placeable.ToLocation {
        val attachments: List<Placeable.ToCard>
    }
}

data class Power(val value: Int)
data class Strength(val value: Int) {
    fun powerRequiredToVanquish() = Power(value)
}

interface VillainCard : Card {
    object Standard {
        data class Effect(
            override val name: Card.Name,
            override val description: Card.Description,
            override val cost: Power
        ) : VillainCard, Card.WithCost

        val EFFECT = ::Effect

        data class Ally(
            override val name: Card.Name,
            override val description: Card.Description,
            override val cost: Power,
            override val strength: Strength,
            override val attachments: List<Card.Placeable.ToCard> = emptyList()
        ) : VillainCard, Card.WithCost, Card.WithStrength, Card.Placeable.ToLocation, Card.CanHaveAttachments {
            fun attachItem(item: Item) = this.copy(attachments = attachments + item)
        }

        val ALLY = ::Ally

        data class Item(
            override val name: Card.Name,
            override val description: Card.Description,
            override val cost: Power,
            val effect: Effect? = null
        ) : VillainCard, Card.WithCost, Card.Placeable.ToLocation, Card.Placeable.ToCard {
            interface Effect {
                data class AddStrengthToAlly(val strength: Strength) : Effect
            }
        }

        val ITEM = ::Item

        data class Condition(
            override val name: Card.Name,
            override val description: Card.Description,
        ) : VillainCard

        val CONDITION = ::Condition
    }

    data class Hand(private val value: List<VillainCard> = emptyList()) : List<VillainCard> by value
    data class Deck(private val value: List<VillainCard> = emptyList()) : List<VillainCard> by value
    data class DiscardPile(private val value: List<VillainCard> = emptyList()) : List<VillainCard> by value

}

interface FateCard : Card {
    companion object Standard {
        data class Effect internal constructor(
            override val name: Card.Name,
            override val description: Card.Description,
        ) : VillainCard

        val EFFECT = ::Effect

        data class Hero internal constructor(
            override val name: Card.Name,
            override val description: Card.Description,
            override val strength: Strength,
            override val attachments: List<Card.Placeable.ToCard> = emptyList()
        ) : VillainCard, Card.WithStrength, Card.Placeable.ToLocation, Card.CanHaveAttachments {
            fun attachItem(item: Item) = this.copy(attachments = attachments + item)
        }

        val HERO = ::Hero

        data class Item internal constructor(
            override val name: Card.Name,
            override val description: Card.Description,
            val effect: Effect? = null
        ) : VillainCard, Card.Placeable.ToLocation, Card.Placeable.ToCard{
            interface Effect {
                data class AddStrengthToHero(val strength: Strength) : Effect
            }
        }

        val ITEM = ::Item
    }

    data class Deck(private val value: List<VillainCard> = emptyList()) : List<VillainCard> by value
    data class DiscardPile(private val value: List<VillainCard> = emptyList()) : List<VillainCard> by value
}

class FateToken

data class Realm(val value: List<Location>) : List<Realm.Location> by value {
    constructor(vararg locations: Location) : this(locations.toList())

    open class Location(
        open val name: Name,
        open val actionSpaceSlots: List<ActionSpaceSlot>,
        open val fateCards: List<FateCard> = emptyList(),
        open val villainCards: List<VillainCard> = emptyList(),
    ) {
        open fun addFateCard(fateCard: FateCard) =
            Location(name, actionSpaceSlots, fateCards + fateCard, villainCards)

        open fun addVillainCard(villainCard: VillainCard) =
            Location(name, actionSpaceSlots, fateCards, villainCards + villainCard)

        data class Name(val value: String) {
            override fun toString() = value.toString()
        }

        sealed interface ActionSpaceSlot {
            val actionSpace: ActionSpace

            data class CoverableActionSpaceSlot(
                override val actionSpace: ActionSpace,
                val isCovered: Boolean = false
            ) : ActionSpaceSlot

            data class NotCoverableActionSpaceSlot(override val actionSpace: ActionSpace) : ActionSpaceSlot
        }

        sealed class LockableLocation(
            override val name: Name,
            override val actionSpaceSlots: List<ActionSpaceSlot>,
            override val fateCards: List<FateCard>,
            override val villainCards: List<VillainCard>
        ) : Location(name, actionSpaceSlots, fateCards, villainCards) {
            abstract fun toggleLock(): LockableLocation
            data class LockedLocation(
                override val name: Name,
                override val actionSpaceSlots: List<ActionSpaceSlot>,
                override val fateCards: List<FateCard>,
                override val villainCards: List<VillainCard>
            ) : LockableLocation(name, actionSpaceSlots, fateCards, villainCards) {
                override fun toggleLock() = UnlockedLocation(name, actionSpaceSlots, fateCards, villainCards)

                override fun addFateCard(fateCard: FateCard) =
                    LockedLocation(name, actionSpaceSlots, fateCards + fateCard, villainCards)

                override fun addVillainCard(villainCard: VillainCard) =
                    LockedLocation(name, actionSpaceSlots, fateCards, villainCards + villainCard)
            }

            data class UnlockedLocation(
                override val name: Name,
                override val actionSpaceSlots: List<ActionSpaceSlot>,
                override val fateCards: List<FateCard>,
                override val villainCards: List<VillainCard>
            ) : LockableLocation(name, actionSpaceSlots, fateCards, villainCards) {
                override fun toggleLock() = LockedLocation(name, actionSpaceSlots, fateCards, villainCards)

                override fun addFateCard(fateCard: FateCard) =
                    UnlockedLocation(name, actionSpaceSlots, fateCards + fateCard, villainCards)

                override fun addVillainCard(villainCard: VillainCard) =
                    UnlockedLocation(name, actionSpaceSlots, fateCards, villainCards + villainCard)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Location) return false

            if (name != other.name) return false
            if (actionSpaceSlots != other.actionSpaceSlots) return false
            if (fateCards != other.fateCards) return false
            if (villainCards != other.villainCards) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + actionSpaceSlots.hashCode()
            result = 31 * result + fateCards.hashCode()
            result = 31 * result + villainCards.hashCode()
            return result
        }

        override fun toString(): String {
            return "Location(name=$name, actionSpaceSlots=$actionSpaceSlots, fateCards=$fateCards, villainCards=$villainCards)"
        }
    }
}

interface ActionSpace {
    val actionName: String

    companion object Standard {
        data class GainPower internal constructor(
            val powerValue: UInt, override val actionName: String = "Gain Power ($powerValue)"
        ) : ActionSpace

        val GAIN_POWER: (UInt) -> GainPower = { GainPower(it) }

        data class PlayCard internal constructor(override val actionName: String = "Play A Card") : ActionSpace

        val PLAY_CARD = { PlayCard() }

        data class Activate internal constructor(override val actionName: String = "Activate") : ActionSpace

        val ACTIVATE = { Activate() }

        data class Fate internal constructor(override val actionName: String = "Fate") : ActionSpace

        val FATE = { Fate() }

        data class MoveAnItemOrAlly internal constructor(override val actionName: String = "Move an Item or Ally") :
            ActionSpace

        val MOVE_AN_ITEM_OR_ALLY = { MoveAnItemOrAlly() }

        data class MoveAHero internal constructor(override val actionName: String = "Move a Hero") : ActionSpace

        val MOVE_A_HERO = { MoveAHero() }

        data class Vanquish internal constructor(override val actionName: String = "Vanquish") : ActionSpace

        val VANQUISH = { Vanquish() }

        data class Discard internal constructor(override val actionName: String = "Discard") : ActionSpace

        val DISCARD = { Discard() }
    }
}

fun ActionSpace.coverable() = Realm.Location.ActionSpaceSlot.CoverableActionSpaceSlot(this)
fun ActionSpace.notCoverable() = Realm.Location.ActionSpaceSlot.NotCoverableActionSpaceSlot(this)
