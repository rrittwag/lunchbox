<template>
  <div class="flex justify-between items-center">
    <h2
      class="flex-grow text-center
             block text-2xl text-neutral-800"
    >
      {{ selectedDayAsWeekday }}
      <time class="block text-lg font-light text-neutral-700" :datetime="selectedDayAsISOString">
        {{ selectedDayAsDateString }}
      </time>
    </h2>

    <button
      class="flex justify-center items-center
             order-first
             w-16 h-16
             disabled:cursor-not-allowed
             text-primary-400 disabled:text-primary-200"
      title="Zu vorherigem Tag wechseln"
      aria-label="Zu vorherigem Tag wechseln"
      @click="goPrevDay"
      :disabled="!prevDay()"
      aria-keyshortcuts="ArrowLeft"
    >
      <AngleLeftIcon class="w-16 h-16" />
    </button>

    <button
      class="flex justify-center items-center
             w-16 h-16
             disabled:cursor-not-allowed
             text-primary-400 disabled:text-primary-200"
      title="Zu nächstem Tag wechseln"
      aria-label="Zu nächstem Tag wechseln"
      @click="goNextDay"
      :disabled="!nextDay()"
      aria-keyshortcuts="ArrowRight"
    >
      <AngleRightIcon class="w-16 h-16" />
    </button>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import AngleLeftIcon from '@/assets/icons/angle-left.svg'
import AngleRightIcon from '@/assets/icons/angle-right.svg'
import { LunchStore } from '@/store/modules/LunchStore'
import { formatToDate, formatToWeekday } from '@/util/formatting'

@Component({
  components: {
    AngleLeftIcon,
    AngleRightIcon,
  },
})
export default class DaySelector extends Vue {
  @Inject() lunchStore!: LunchStore

  mounted() {
    window.addEventListener('keydown', this.handleKeydown)
  }

  destroyed() {
    window.removeEventListener('keydown', this.handleKeydown)
  }

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

  get selectedDayAsWeekday(): string {
    return formatToWeekday(this.lunchStore.selectedDay)
  }

  get selectedDayAsDateString(): string {
    return formatToDate(this.lunchStore.selectedDay)
  }

  get selectedDayAsISOString(): string {
    return this.lunchStore.selectedDay.toISOString().substring(0, 10)
  }

  handleKeydown(event: KeyboardEvent) {
    if (event.defaultPrevented) {
      return // Do nothing if the event was already processed
    }

    switch (event.key) {
      case 'Left': // IE/Edge specific value
      case 'ArrowLeft':
        this.goPrevDay()
        break
      case 'Right': // IE/Edge specific value
      case 'ArrowRight':
        this.goNextDay()
        break
      default:
        return // Quit when this doesn't handle the key event.
    }

    // Cancel the default action to avoid it being handled twice
    event.preventDefault()
  }
}
</script>
