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
        class="flex h-12 w-12 items-center justify-center [&>svg]:h-8 [&>svg]:w-8"
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
