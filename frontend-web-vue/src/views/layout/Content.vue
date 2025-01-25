<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<template>
  <main class="h-full w-full">
    <RouterView v-slot="{ Component }">
      <Transition
        mode="out-in"
        leave-active-class="transition-opacity duration-150"
        leave-to-class="opacity-0"
        enter-active-class="transition-opacity duration-150"
        enter-from-class="opacity-0"
      >
        <component :is="Component" />
      </Transition>
    </RouterView>
  </main>
</template>
