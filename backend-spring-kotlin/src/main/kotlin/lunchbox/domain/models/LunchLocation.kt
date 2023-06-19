package lunchbox.domain.models

/**
 * Ort eines Mittagsanbieters.
 */
enum class LunchLocation(
  val label: String,
  val bundesland: Bundesland,
) {
  NEUBRANDENBURG("Neubrandenburg", Bundesland.MECK_POMM),
  BERLIN_SPRINGPFUHL("Berlin Springpfuhl", Bundesland.BERLIN),
}

enum class Bundesland(
  val kuerzel: String,
) {
  MECK_POMM("MV"),
  BERLIN("BE"),
}
