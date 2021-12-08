<template>
  <button
    class="
      text-primary-500
      hover:text-primary-600
      origin-center
      transform
      hover:scale-110
      disabled:text-primary-400 disabled:opacity-25 disabled:cursor-not-allowed
    "
    :class="{ 'active:text-primary-800': !props.disabled }"
    :title="title"
    :aria-label="title"
    :disabled="props.disabled"
    :aria-keyshortcuts="isPrevious ? 'ArrowLeft' : 'ArrowRight'"
    @click="onClick"
  >
    <AngleLeftIcon v-if="isPrevious" class="w-16 h-16" />
    <AngleRightIcon v-else class="w-16 h-16" />
  </button>
</template>

<script setup lang="ts">
import AngleLeftIcon from '@/assets/icons/angle-left.svg'
import AngleRightIcon from '@/assets/icons/angle-right.svg'
import { defineEmits, defineProps } from 'vue'
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
