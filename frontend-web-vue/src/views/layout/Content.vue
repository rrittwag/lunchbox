<template>
  <main class="h-full w-full">
    <RouterView v-slot="{ Component }">
      <Transition
        mode="out-in"
        leaveActiveClass="transition-opacity duration-150"
        leaveToClass="opacity-0"
        enterActiveClass="transition-opacity duration-150"
        enterFromClass="opacity-0"
      >
        <component :is="Component" />
      </Transition>
    </RouterView>
  </main>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>
