<template>
  <div class="relative flex min-h-screen w-full max-w-4md flex-col font-sans antialiased">
    <Header />
    <Content class="grow" />
  </div>
</template>

<script setup lang="ts">
import Content from '@/views/layout/Content.vue'
import Header from '@/views/layout/Header.vue'
import { useTheme } from '@/store/theme'

useTheme()
</script>
