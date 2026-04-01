<script setup lang="ts">
const props = defineProps<{
  href: string
  text: string
  target?: '_blank' | '_self' | '_parent' | '_top'
}>()
const after = 'after:absolute after:left-0 after:top-0 after:-z-10 after:h-full after:w-full after:rounded-full after:bg-primary-400 '
const active = 'active:scale-95 active:transition active:after:scale-x-125 active:after:scale-y-150 active:after:opacity-0 active:after:transition active:after:duration-500'
const inlineClass = `relative z-0 h-12 rounded-full bg-primary-400 px-6 text-neutral-50 ${after} ${active}`
</script>

<template>
  <p>
    <a :class="inlineClass" :href="props.href" :target="props.target ?? '_blank'" role="button">{{ props.text }}</a>
  </p>
</template>
