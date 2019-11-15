<template>
  <b-row class="day-selector pb-4">
    <b-col cols="12" sm="9" md="6" lg="4">
      <b-row align-v="center">
        <b-col cols="2">
          <b-button variant="primary" size="lg" @click="goPrevDay" :disabled="!prevDay()">
            <v-icon name="angle-left" scale="3" />
          </b-button>
        </b-col>

        <b-col cols="8" class="text-center bg-red-500">
          <h2>
            <small>{{ lunchStore.selectedDay | formatToWeekday }}</small>
          </h2>
          <h2 class="mt-0 mb-1">{{ lunchStore.selectedDay | formatToDate }}</h2>
        </b-col>

        <b-col cols="2">
          <b-button
            class="float-right"
            variant="primary"
            size="lg"
            @click="goNextDay"
            :disabled="!nextDay()"
          >
            <v-icon name="angle-right" scale="3" />
          </b-button>
        </b-col>
      </b-row>
    </b-col>
  </b-row>
</template>

<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import { LunchStore } from '@/store'
import 'vue-awesome/icons/angle-left'
import 'vue-awesome/icons/angle-right'

@Component
export default class DaySelector extends Vue {
  @Inject() lunchStore!: LunchStore

  get lunchDays(): Date[] {
    const providerIDsForSelectedLocation = this.lunchStore.providers
      .filter(p => p.location === this.lunchStore.selectedLocation.name)
      .map(p => p.id)
    const lunchDays: string[] = this.lunchStore.offers
      .filter(o => providerIDsForSelectedLocation.includes(o.provider))
      .map(o => o.day)
    return Array.from(new Set<string>(lunchDays))
      .map(dayString => new Date(dayString))
      .sort((day1, day2) => day1.getTime() - day2.getTime())
  }

  prevDay(): Date | undefined {
    return this.lunchDays.filter(day => day < this.lunchStore.selectedDay).pop()
  }

  nextDay(): Date | undefined {
    return this.lunchDays.filter(day => day > this.lunchStore.selectedDay)[0]
  }

  goPrevDay(): void {
    const prevDay = this.prevDay()
    if (prevDay) this.lunchStore.setSelectedDay(prevDay)
  }

  goNextDay(): void {
    const nextDay = this.nextDay()
    if (nextDay) this.lunchStore.setSelectedDay(nextDay)
  }
}
</script>
