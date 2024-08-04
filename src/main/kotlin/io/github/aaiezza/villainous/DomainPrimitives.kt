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

enum class VillainousExpansion(val value: String) {
    THE_WORST_TAKES_IT_ALL("The Worst Takes it All"),
    INTRODUCTION_TO_EVIL("Introduction to Evil"),
    WICKED_TO_THE_CORE("Wicked to the Core"),
    EVIL_COMES_PREPARED("Evil Comes Prepared"),
    PERFECTLY_WRETCHED("Perfectly Wretched"),
    DESPICABLE_PLOTS("Despicable Plots"),
    FILLED_WITH_FRIGHT("Filled With Fright"),
    SUGAR_AND_SPITE("Sugar and Spite");
}

data class VillainCharacter(val name: Name, val objective: Objective, val villainousExpansion: VillainousExpansion) {
    data class Name(val value: String)
    data class Objective(val value: String)

    class VillainMover
}

interface Card {
    val name: Name
    val description: Description

    data class Name(val value: String)
    data class Description(val value: String)

    sealed interface Cost {
        data class Known(val value: Power) : Cost
        class Variable : Cost
    }

    interface WithCost : Card {
        val cost: Cost
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

    interface ProvidesActions : Placeable.ToLocation {
        val actionSpaceSlots: List<Realm.Location.ActionSpaceSlot>
    }
}

fun CardCost(powerCost: Int) = Card.Cost.Known(Power(powerCost))

data class Power(val value: Int)
data class Strength(val value: Int) {
    fun powerRequiredToVanquish() = Power(value)
}

interface VillainCard : Card {
    object Standard {
        data class Effect(
            override val name: Card.Name,
            override val description: Card.Description,
            override val cost: Card.Cost
        ) : VillainCard, Card.WithCost

        val EFFECT = ::Effect

        open class Ally(
            override val name: Card.Name,
            override val description: Card.Description,
            override val cost: Card.Cost,
            override val strength: Strength,
            override val attachments: List<Card.Placeable.ToCard> = emptyList(),
            override val actionSpaceSlots: List<Realm.Location.ActionSpaceSlot> = emptyList()
        ) : VillainCard, Card.WithCost, Card.WithStrength, Card.Placeable.ToLocation, Card.CanHaveAttachments,
            Card.ProvidesActions {
            fun attachItem(item: Item) = this.copy(attachments = attachments + item)

            fun copy(
                name: Card.Name = this.name,
                description: Card.Description = this.description,
                cost: Card.Cost = this.cost,
                strength: Strength = this.strength,
                attachments: List<Card.Placeable.ToCard> = this.attachments,
                actionSpaceSlots: List<Realm.Location.ActionSpaceSlot> = this.actionSpaceSlots
            ) = Ally(name, description, cost, strength, attachments, actionSpaceSlots)

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is Ally) return false

                if (name != other.name) return false
                if (description != other.description) return false
                if (cost != other.cost) return false
                if (strength != other.strength) return false
                if (attachments != other.attachments) return false
                if (actionSpaceSlots != other.actionSpaceSlots) return false

                return true
            }

            override fun hashCode(): Int {
                var result = name.hashCode()
                result = 31 * result + description.hashCode()
                result = 31 * result + cost.hashCode()
                result = 31 * result + strength.hashCode()
                result = 31 * result + attachments.hashCode()
                result = 31 * result + actionSpaceSlots.hashCode()
                return result
            }

            override fun toString(): String {
                return "Ally(name=$name, description=$description, cost=$cost, strength=$strength, attachments=$attachments, actionSpaceSlots=$actionSpaceSlots)"
            }
        }

        val ALLY = ::Ally

        data class Item(
            override val name: Card.Name,
            override val description: Card.Description,
            override val cost: Card.Cost,
            val effect: Effect? = null,
            override val actionSpaceSlots: List<Realm.Location.ActionSpaceSlot> = emptyList()
        ) : VillainCard, Card.WithCost, Card.Placeable.ToLocation, Card.Placeable.ToCard, Card.ProvidesActions {
            // TODO: Item effects based on their presence need to be re-engineered.
            // Need more domain instances.
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
        ) : VillainCard, Card.Placeable.ToLocation, Card.Placeable.ToCard {
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

        sealed class Lockable(
            override val name: Name,
            override val actionSpaceSlots: List<ActionSpaceSlot>,
            override val fateCards: List<FateCard> = emptyList(),
            override val villainCards: List<VillainCard> = emptyList(),
        ) : Location(name, actionSpaceSlots, fateCards, villainCards) {
            abstract fun toggleLock(): Lockable
            data class Locked(
                override val name: Name,
                override val actionSpaceSlots: List<ActionSpaceSlot>,
                override val fateCards: List<FateCard> = emptyList(),
                override val villainCards: List<VillainCard> = emptyList(),
            ) : Lockable(name, actionSpaceSlots, fateCards, villainCards) {
                override fun toggleLock() = Unlocked(name, actionSpaceSlots, fateCards, villainCards)

                override fun addFateCard(fateCard: FateCard) =
                    Locked(name, actionSpaceSlots, fateCards + fateCard, villainCards)

                override fun addVillainCard(villainCard: VillainCard) =
                    Locked(name, actionSpaceSlots, fateCards, villainCards + villainCard)
            }

            data class Unlocked(
                override val name: Name,
                override val actionSpaceSlots: List<ActionSpaceSlot>,
                override val fateCards: List<FateCard> = emptyList(),
                override val villainCards: List<VillainCard> = emptyList(),
            ) : Lockable(name, actionSpaceSlots, fateCards, villainCards) {
                override fun toggleLock() = Locked(name, actionSpaceSlots, fateCards, villainCards)

                override fun addFateCard(fateCard: FateCard) =
                    Unlocked(name, actionSpaceSlots, fateCards + fateCard, villainCards)

                override fun addVillainCard(villainCard: VillainCard) =
                    Unlocked(name, actionSpaceSlots, fateCards, villainCards + villainCard)
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

    data class CostToUse(val powerCost: Power)

    interface WithCost : ActionSpace {
        val cost: CostToUse
    }

    companion object Standard {
        data class GainPower internal constructor(
            val powerValue: UInt, override val actionName: String = "Gain Power ($powerValue)"
        ) : ActionSpace

        val GAIN_POWER: (UInt) -> GainPower = { GainPower(it) }

        data class PlayCard internal constructor(override val actionName: String = "Play A Card") : ActionSpace

        val PLAY_CARD = { PlayCard() }

        data class Activate internal constructor(override val actionName: String = "Activate") : ActionSpace
        fun Activate.withCost(powerCost: Int) = ActivateAtCost(cost = CostToUse(Power(powerCost)))
        data class ActivateAtCost internal constructor(override val actionName: String = "Activate", override val cost: CostToUse) : WithCost

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
