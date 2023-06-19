package lunchbox.app

import lunchbox.util.date.DateValidator
import lunchbox.util.date.FeiertageValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigLunchResolver(val feiertageValidator: FeiertageValidator) {
  @Bean
  fun dateValidator() =
    DateValidator.validFromMondayLastWeek().and(feiertageValidator)
}
