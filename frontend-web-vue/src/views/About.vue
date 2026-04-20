<script setup lang="ts">
import Credits from '@/views/about/Credits.vue'
import Development from '@/views/about/Development.vue'
import Jumbotron from '@/views/about/Jumbotron.vue'
import Providers from '@/views/about/Providers.vue'
</script>

<template>
  <div class="container text-neutral-900">
    <div header />
    <div class="flex">
      <div class="w-2xl" />
      <div>
        <Jumbotron />
        <div class="grid grid-cols-3 gap-4">
          <Providers />
          <Development />
          <Credits />
        </div>
      </div>
    </div>
    <div footer />
  </div>
</template>
