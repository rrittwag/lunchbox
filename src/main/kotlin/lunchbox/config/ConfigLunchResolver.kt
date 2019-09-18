package lunchbox.config

import lunchbox.domain.resolvers.DateValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigLunchResolver {
  @Bean
  fun dateValidator() =
    DateValidator.validFromMondayLastWeek()
}
