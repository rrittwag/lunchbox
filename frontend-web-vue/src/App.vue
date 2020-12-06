<template>
  <div id="app" class="flex justify-center">
    <Layout />
  </div>
</template>

<script setup lang="ts">
import Layout from '/@/views/Layout.vue'
import { useLunchStore } from '/@/store/lunch'

const { loadFromApi } = useLunchStore()
loadFromApi()
</script>
