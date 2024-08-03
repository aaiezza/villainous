package io.github.aaiezza.villainous

import io.github.aaiezza.villainous.game.Game
import io.github.aaiezza.villainous.game.Players

/* Generic */

inline fun <K, V, RK, RV> Map<K, V>.mapIndexed(transform: (index: Int, Pair<K, V>) -> Pair<RK, RV>): Map<RK, RV> =
    this
        .toList()
        .mapIndexed(transform)
        .toMap()
