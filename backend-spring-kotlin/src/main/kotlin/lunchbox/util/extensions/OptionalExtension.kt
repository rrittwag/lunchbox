package lunchbox.util.extensions

import java.util.Optional

/**
 * Extension für Umwandlung von Kotlins Nullable in Javas Optional.
 */
fun <T> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

/**
 * Extension für Umwandlung von Javas Optional in Kotlins Nullable.
 */
fun <T> Optional<T>.orNull(): T? = orElse(null)
