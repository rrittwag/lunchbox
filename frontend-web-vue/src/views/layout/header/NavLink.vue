<script setup lang="ts">
import { RouterLink } from 'vue-router'

const props = defineProps<{
  to: string
}>()
</script>

<template>
  <li class="inline-block px-2">
    <RouterLink v-slot="{ navigate, isExactActive, route }" :to="props.to" custom>
      <a
        class="nav-link flex h-12 w-12 items-center justify-center"
        :class="isExactActive ? 'text-neutral-800' : 'text-neutral-300'"
        :href="route.path"
        :title="route.meta.title"
        :aria-label="route.meta.title"
        :aria-current="isExactActive ? 'page' : undefined"
        @click="navigate"
      >
        <slot />
      </a>
    </RouterLink>
  </li>
</template>

<style lang="scss">
.nav-link > svg {
  @apply h-8 w-8;
}
</style>
