<template>
  <b-nav-item-dropdown
        class="d-none d-sm-block"
        v-bind:text="lunchStore.selectedLocation.shortName"
  >
    <b-dropdown-item
          v-for="location in lunchStore.locations"
          v-bind:key="location.shortName"
          v-on:click="lunchStore.mutateSelectedLocation(location)"
          v-bind:active="location === lunchStore.selectedLocation"
    >
      {{ location.name }}
    </b-dropdown-item>
  </b-nav-item-dropdown>
</template>



<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import LunchStore from '@/store/lunch'

@Component
export default class LocationSelector extends Vue {
  private lunchStore: LunchStore = getModule(LunchStore)
}
</script>
