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
      @click="prevClicked"
      :disabled="disabledPrev"
    />
    <DaySelectorButton direction="next" @click="nextClicked" :disabled="disabledNext" />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { formatToLocalDate, formatToISODate, formatToWeekday } from '@/util/formatting'
import DaySelectorButton from '@/views/offers/dayselector/DaySelectorButton.vue'
import { DaySelectorDirection } from '@/views/offers/dayselector/DaySelectorDirection'

@Component({
  components: { DaySelectorButton },
})
export default class DaySelector extends Vue {
  @Prop() selectedDay!: Date
  @Prop() disabledPrev!: boolean
  @Prop() disabledNext!: boolean

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

    if (event.altKey || event.ctrlKey || event.shiftKey || event.metaKey) return

    switch (event.key) {
      case 'Left': // IE/Edge specific value
      case 'ArrowLeft':
        this.prevClicked()
        break
      case 'Right': // IE/Edge specific value
      case 'ArrowRight':
        this.nextClicked()
        break
      default:
        return // Quit when this doesn't handle the key event.
    }

    // Cancel the default action to avoid it being handled twice
    event.preventDefault()
  }

  prevClicked(): void {
    this.$emit('change', DaySelectorDirection.PREVIOUS)
  }

  nextClicked(): void {
    this.$emit('change', DaySelectorDirection.NEXT)
  }
}
</script>
