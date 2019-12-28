<template>
  <div
    class="w-full sm:w-3/4 md:w-1/2 lg:w-1/3
           px-4 py-2"
  >
    <div class="flex items-center">
      <div class="w-1/6">
        <button class="text-red-500" @click="goPrevDay" :disabled="!prevDay()">
          <v-icon name="angle-left" scale="3" />
        </button>
      </div>

      <div class="w-2/3 text-center flex-grow">
        <h2 class="text-3xl">
          <small>{{ lunchStore.selectedDay | formatToWeekday }}</small>
        </h2>
        <h2 class="text-lg mb-1">{{ lunchStore.selectedDay | formatToDate }}</h2>
      </div>

      <div class="w-1/6">
        <button class="float-right text-red-500" @click="goNextDay" :disabled="!nextDay()">
          <v-icon name="angle-right" scale="3" />
        </button>
      </div>
    </div>
  </div>
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
