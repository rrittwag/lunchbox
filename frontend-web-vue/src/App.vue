<script setup lang="ts">
import { useLunchStore } from '@/store/lunch'
import Layout from '@/views/Layout.vue'

useLunchStore().loadFromApi()
</script>

<template>
  <Layout />
</template>
