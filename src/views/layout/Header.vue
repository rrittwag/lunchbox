<template>
  <div
    class="flex flex-wrap justify-between items-center
           px-4 py-4"
  >
    <DaySelector class="w-full sm:max-w-sm" />

    <div
      class="flex justify-center
             w-full sm:w-auto
             order-first md:order-last
             px-4"
    >
      <router-link to="/" exact class="px-1">Mittagsangebote</router-link>
      <router-link to="/settings" class="px-1">Einstellungen</router-link>
      <router-link to="/about" class="px-1">Info</router-link>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import LocationSelector from './LocationSelector.vue'
import DaySelector from '@/views/offers/DaySelector.vue'

@Component({
  components: {
    DaySelector,
    LocationSelector,
  },
})
export default class Header extends Vue {}
</script>
