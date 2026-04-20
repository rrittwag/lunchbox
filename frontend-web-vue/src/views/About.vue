<script setup lang="ts">
import credits from '@/views/about/Credits.vue'
import dev from '@/views/about/Developement.vue'
import jumbotron from '@/views/about/Jumbotron.vue'
import providers from '@/views/about/Provider.vue'
</script>

<template>
  <div class="container text-neutral-900">
    <div header />
    <div class="flex">
      <div class="w-2xl" />
      <div>
        <jumbotron />
        <div class="grid grid-cols-3 gap-4">
          <providers />
          <dev />
          <credits />
        </div>
      </div>
    </div>
    <div footer />
  </div>
</template>
