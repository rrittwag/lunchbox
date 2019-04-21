<template>
  <b-nav-item-dropdown
        class="location-selector d-none d-sm-block"
        :text="lunchStore.selectedLocation.shortName"
  >
    <b-dropdown-item
          v-for="location in lunchStore.locations"
          :key="location.shortName"
          @click="lunchStore.setSelectedLocation(location)"
          :active="location === lunchStore.selectedLocation"
    >
      {{ location.name }}
    </b-dropdown-item>
  </b-nav-item-dropdown>
</template>



<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import { LunchStore } from '@/store'

@Component
export default class LocationSelector extends Vue {
  @Inject() lunchStore!: LunchStore
}
</script>
