<script setup lang="ts">
const props = defineProps<{
  href: string
  text: string
  target?: '_blank' | '_self' | '_parent' | '_top'
  noTextWrap?: boolean
  inline?: boolean
}>()
// Defaults: text-blue-600 hover:text-blue-800 visited:text-purple-600 see https://stackoverflow.com/questions/63883580/tailwind-css-how-to-style-a-href-links-in-react
const inlineClass = 'text-primary-800 hover:text-primary-400  visited:text-accent-800 decoration-dotted underline'
</script>

<template>
  <span :class="{ 'text-nowrap': noTextWrap }">
    <a :class="inlineClass" :href="props.href" :target="props.target ?? '_blank'">{{ props.text }}</a>
  </span>
  <br v-if="!inline">
</template>
