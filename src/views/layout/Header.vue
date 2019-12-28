<template>
  <div class="flex flex-wrap items-center w-full">
    <DaySelector class="w-full sm:w-1/2" />
    <div
      class="flex-grow order-first md:order-last
             w-full sm:w-1/2
             px-4"
    >
      <div class="md:float-right">
        <router-link to="/" exact class="px-1">Mittagsangebote</router-link>
        <router-link to="/settings" class="px-1">Einstellungen</router-link>
        <router-link to="/about" class="px-1">Info</router-link>
      </div>
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
