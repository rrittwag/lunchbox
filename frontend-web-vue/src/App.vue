<template>
  <Layout />
</template>

<script setup lang="ts">
import Layout from '@/views/Layout.vue'
import { useLunchStore } from '@/store/lunch'

useLunchStore().loadFromApi()
</script>
