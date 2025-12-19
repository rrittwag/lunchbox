package lunchbox.util.json

import com.fasterxml.jackson.annotation.JsonInclude
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.kotlinModule

/**
 * Erstellt einen vorkonfigurierten Jackson ObjectMapper.
 */
fun createObjectMapper(): ObjectMapper =
  JsonMapper
    .builder()
    .changeDefaultPropertyInclusion { it.withValueInclusion(JsonInclude.Include.NON_NULL) }
    .addModule(kotlinModule())
    .build()
