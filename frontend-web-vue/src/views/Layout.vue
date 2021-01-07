<template>
  <div
    class="flex flex-col
           w-full max-w-4md min-h-screen
           font-sans antialiased
           relative"
  >
    <Header />
    <Content class="flex-grow" />
  </div>
</template>

<script setup lang="ts">
import Content from '@/views/layout/Content.vue'
import Header from '@/views/layout/Header.vue'
import { useTheme } from '@/store/theme'

useTheme()
</script>
