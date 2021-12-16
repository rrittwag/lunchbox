<template>
  <div class="rounded-full font-light text-neutral-900 px-3 py-1 mr-1" :class="props.color">
    {{ props.label }}
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  label: string
  color: string
}>()
</script>
