<template>
  <div
    class="rounded-full
           font-light text-neutral-900
           px-3 py-1 mr-1"
    :class="props.color"
  >
    {{ props.label }}
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
export default defineComponent({ name: 'Badge' })
</script>

<script setup lang="ts">
import { defineProps } from 'vue'

const props = defineProps<{
  label: string
  color: string
}>()
</script>
