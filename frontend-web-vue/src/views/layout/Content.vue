<template>
  <main class="w-full h-full">
    <router-view v-slot="{ Component }">
      <transition
        mode="out-in"
        leave-active-class="transition-opacity duration-150"
        leave-to-class="opacity-0"
        enter-active-class="transition-opacity duration-150"
        enter-from-class="opacity-0"
      >
        <component :is="Component" />
      </transition>
    </router-view>
  </main>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
export default defineComponent({ name: 'Content' })
</script>
