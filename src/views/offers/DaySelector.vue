<template>
  <div class="flex justify-between items-center">
    <button
      class="flex justify-center items-center
             w-16 h-16"
      title="Zu vorherigem Tag wechseln"
      aria-label="Zu vorherigem Tag wechseln"
      @click="goPrevDay"
      :disabled="!prevDay()"
    >
      <AngleLeftIcon class="w-16 h-16 text-primary-400" />
    </button>

    <div class="flex-grow text-center">
      <h2 class="text-2xl text-neutral-900">
        {{ lunchStore.selectedDay | formatToWeekday }}
      </h2>
      <h2 class="text-lg font-light text-neutral-700">
        {{ lunchStore.selectedDay | formatToDate }}
      </h2>
    </div>

    <button
      class="flex justify-center items-center
             w-16 h-16"
      title="Zu nächstem Tag wechseln"
      aria-label="Zu nächstem Tag wechseln"
      @click="goNextDay"
      :disabled="!nextDay()"
    >
      <AngleRightIcon class="w-16 h-16 text-primary-400" />
    </button>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import { LunchStore } from '@/store'
import AngleLeftIcon from '@/assets/icons/angle-left.svg'
import AngleRightIcon from '@/assets/icons/angle-right.svg'

@Component({
  components: {
    AngleLeftIcon,
    AngleRightIcon,
  },
})
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
