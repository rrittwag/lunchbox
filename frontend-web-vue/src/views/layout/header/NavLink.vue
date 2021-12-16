<template>
  <li class="inline-block px-2">
    <RouterLink v-slot="{ navigate, isExactActive, route }" :to="props.to" custom>
      <a
        class="nav-link flex justify-center items-center w-12 h-12"
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

<script setup lang="ts">
const props = defineProps<{
  to: string
}>()
</script>

<style lang="scss">
.nav-link > svg {
  @apply w-8 h-8;
}
</style>
