<template>
  <b-nav-item-dropdown
        class="d-none d-sm-block"
        :text="lunchStore.selectedLocation.shortName"
  >
    <b-dropdown-item
          v-for="location in lunchStore.locations"
          :key="location.shortName"
          @click="lunchStore.mutateSelectedLocation(location)"
          :active="location === lunchStore.selectedLocation"
    >
      {{ location.name }}
    </b-dropdown-item>
  </b-nav-item-dropdown>
</template>



<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import LunchStore from '@/store/LunchStore'

@Component
export default class LocationSelector extends Vue {
  @Inject() lunchStore!: LunchStore
}
</script>
