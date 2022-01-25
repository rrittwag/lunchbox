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
    <AngleLeftIcon v-if="isPrevious" class="h-16 w-16" />
    <AngleRightIcon v-else class="h-16 w-16" />
  </button>
</template>

<script setup lang="ts">
import AngleLeftIcon from '@/assets/icons/angle-left.svg'
import AngleRightIcon from '@/assets/icons/angle-right.svg'
import { DaySelectorDirection } from '@/views/offers/dayselector/DaySelectorDirection'

const props = defineProps<{
  disabled?: boolean
  direction: DaySelectorDirection
}>()
const emit = defineEmits<{
  (e: 'click'): void
}>()

const onClick = () => emit('click')

const isPrevious = props.direction === DaySelectorDirection.PREVIOUS
const title = isPrevious ? 'Zu vorherigem Tag wechseln' : 'Zu nächstem Tag wechseln'
</script>
