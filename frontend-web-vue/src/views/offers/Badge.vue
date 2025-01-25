<script setup lang="ts">
const props = defineProps<{
  label: string
  color: string
}>()
</script>

<template>
  <div class="mr-1 rounded-full px-3 py-1 font-light text-neutral-900" :class="props.color">
    {{ props.label }}
  </div>
</template>
