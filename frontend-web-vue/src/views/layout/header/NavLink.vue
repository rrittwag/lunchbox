<template>
  <li class="inline-block px-2">
    <router-link :to="props.to" custom v-slot="{ navigate, isExactActive, route }">
      <a
        class="nav-link flex justify-center items-center
               w-12 h-12"
        :class="isExactActive ? 'text-neutral-800' : 'text-neutral-300'"
        :href="route.path"
        :title="route.meta.title"
        :aria-label="route.meta.title"
        :aria-current="isExactActive ? 'page' : null"
        @click="navigate"
      >
        <slot />
      </a>
    </router-link>
  </li>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'

const props = defineProps<{
  to: string
}>()
</script>

<style lang="scss">
.nav-link > svg {
  @apply w-8 h-8;
}
</style>
