<template>
  <router-link :to="props.to" :exact="props.exact" v-slot="{ navigate, isExactActive, route }">
    <li class="inline-block px-2">
      <a
        class="nav-link flex justify-center items-center
               w-12 h-12"
        :class="isExactActive ? 'text-neutral-800' : 'text-neutral-300'"
        :href="to"
        :title="route.meta.title"
        :aria-label="route.meta.title"
        :aria-current="isExactActive ? 'page' : null"
        @click="navigate"
      >
        <slot />
      </a>
    </li>
  </router-link>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'

const props = defineProps<{
  to: string
  exact: boolean
}>()
</script>

<style lang="scss">
.nav-link > svg {
  @apply w-8 h-8;
}
</style>
