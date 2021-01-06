<template>
  <div class="flex justify-between items-center">
    <h2 class="flex-grow text-center block text-xl text-neutral-800">
      {{ selectedDayAsWeekday }}
      <time class="block text-lg font-light text-neutral-700" :datetime="selectedDayAsISOString">
        {{ selectedDayAsDateString }}
      </time>
    </h2>

    <DaySelectorButton
      class="order-first"
      direction="prev"
      @click="onClickPrev"
      :disabled="props.disabledPrev"
    />
    <DaySelectorButton direction="next" @click="onClickNext" :disabled="props.disabledNext" />
  </div>
</template>

<script setup lang="ts">
import { computed, defineProps, defineEmit, onMounted, onUnmounted } from 'vue'
import { formatToLocalDate, formatToISODate, formatToWeekday } from '/@/util/formatting'
import DaySelectorButton from '/@/views/offers/dayselector/DaySelectorButton.vue'
import { DaySelectorDirection } from '/@/views/offers/dayselector/DaySelectorDirection'

const props = defineProps<{
  selectedDay: Date
  disabledPrev: boolean
  disabledNext: boolean
}>()
const emit = defineEmit<(e: 'change', direction: DaySelectorDirection) => void>()

const selectedDayAsWeekday = computed(() => formatToWeekday(props.selectedDay))
const selectedDayAsDateString = computed(() => formatToLocalDate(props.selectedDay))
const selectedDayAsISOString = computed(() => formatToISODate(props.selectedDay))

const onClickPrev = () => emit('change', DaySelectorDirection.PREVIOUS)
const onClickNext = () => emit('change', DaySelectorDirection.NEXT)

onMounted(() => window.addEventListener('keydown', onKeydown))
onUnmounted(() => window.removeEventListener('keydown', onKeydown))

function onKeydown(event: KeyboardEvent) {
  if (event.defaultPrevented) {
    return // Do nothing if the event was already processed
  }

  if (event.altKey || event.ctrlKey || event.shiftKey || event.metaKey) return

  switch (event.key) {
    case 'Left': // IE/Edge specific value
    case 'ArrowLeft':
      onClickPrev()
      break
    case 'Right': // IE/Edge specific value
    case 'ArrowRight':
      onClickNext()
      break
    default:
      return // Quit when this doesn't handle the key event.
  }

  // Cancel the default action to avoid it being handled twice
  event.preventDefault()
}
</script>
