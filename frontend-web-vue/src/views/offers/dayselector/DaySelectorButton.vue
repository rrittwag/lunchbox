<template>
  <button
    class="origin-center text-primary-500 hover:scale-110 hover:text-primary-600 disabled:cursor-not-allowed disabled:text-primary-400 disabled:opacity-25"
    :class="{ 'active:text-primary-800': !props.disabled }"
    :title="title"
    :aria-label="title"
    :disabled="props.disabled"
    :aria-keyshortcuts="isPrevious ? 'ArrowLeft' : 'ArrowRight'"
    @click="onClick"
  >
    <AngleLeftIcon v-if="isPrevious" class="h-16 w-16" aria-hidden="true" />
    <AngleRightIcon v-else class="h-16 w-16" aria-hidden="true" />
  </button>
</template>

<script setup lang="ts">
import AngleLeftIcon from '~icons/fa-solid/angle-left'
import AngleRightIcon from '~icons/fa-solid/angle-right'
import {
  DaySelectorDirection,
  LABEL_GO_TO_NEXT_DAY,
  LABEL_GO_TO_PREVIOUS_DAY,
} from '@/views/offers/dayselector/DaySelector.values'

const props = defineProps<{
  disabled?: boolean
  direction: DaySelectorDirection
}>()
const emit = defineEmits<{
  (e: 'click'): void
}>()

const onClick = () => emit('click')

const isPrevious = props.direction === DaySelectorDirection.PREVIOUS
const title = isPrevious ? LABEL_GO_TO_PREVIOUS_DAY : LABEL_GO_TO_NEXT_DAY
</script>
