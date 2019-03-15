package lunchbox.domain.logic

import org.joda.money.Money
import java.time.LocalDate

fun date(dateStr: String): LocalDate = LocalDate.parse(dateStr)

fun euro(moneyStr: String): Money = Money.parse("EUR $moneyStr")
