<template>
  <div class="flex justify-between items-center">
    <h2
      class="flex-grow text-center
             block text-xl text-neutral-800"
    >
      {{ selectedDayAsWeekday }}
      <time class="block text-lg font-light text-neutral-700" :datetime="selectedDayAsISOString">
        {{ selectedDayAsDateString }}
      </time>
    </h2>

    <DaySelectorButton
      class="order-first"
      direction="prev"
      @click="goPrevDay"
      :disabled="!prevDay()"
    />
    <DaySelectorButton direction="next" @click="goNextDay" :disabled="!nextDay()" />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { formatToLocalDate, formatToISODate, formatToWeekday } from '@/util/formatting'
import DaySelectorButton from '@/views/offers/dayselector/DaySelectorButton.vue'
import { DaySelectorDirection, DaySelectorEvent } from '@/views/offers/dayselector/DaySelectorEvent'

@Component({
  components: { DaySelectorButton },
})
export default class DaySelector extends Vue {
  @Prop() selectedDay!: Date
  @Prop() days!: Date[]

  get selectedDayAsWeekday(): string {
    return formatToWeekday(this.selectedDay)
  }

  get selectedDayAsDateString(): string {
    return formatToLocalDate(this.selectedDay)
  }

  get selectedDayAsISOString(): string {
    return formatToISODate(this.selectedDay)
  }

  mounted() {
    window.addEventListener('keydown', this.handleKeydown)
  }

  destroyed() {
    window.removeEventListener('keydown', this.handleKeydown)
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

  prevDay(): Date | undefined {
    return this.days.filter(day => day < this.selectedDay).pop()
  }

  nextDay(): Date | undefined {
    return this.days.filter(day => day > this.selectedDay)[0]
  }

  goPrevDay(): void {
    const prevDay = this.prevDay()
    if (!prevDay) return

    const event: DaySelectorEvent = {
      direction: DaySelectorDirection.PREVIOUS,
      day: prevDay,
    }
    this.$emit('change', event)
  }

  goNextDay(): void {
    const nextDay = this.nextDay()
    if (!nextDay) return

    const event: DaySelectorEvent = {
      direction: DaySelectorDirection.NEXT,
      day: nextDay,
    }
    this.$emit('change', event)
  }
}
</script>
