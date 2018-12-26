<template>
  <b-container fluid>
      <b-row>
        <b-col
          sm='6' md='4' lg='3'
          v-for='provider in visibleProviders()'
          :key='provider.id'
        >
          <OffersOfProvider :provider='provider' />
        </b-col>
      </b-row>
  </b-container>
</template>



<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import { LunchStore } from '@/store'
import { LunchProvider, LunchOffer } from '@/model'
import OffersOfProvider from './OffersOfProvider.vue'

@Component({
  components: {
    OffersOfProvider,
  },
})
export default class Offers extends Vue {
  @Inject() lunchStore!: LunchStore

  visibleProviders(): LunchProvider[] {
    const offersForDay: LunchOffer[] = this.lunchStore.offers
                              .filter(o => new Date(o.day).getTime() === this.lunchStore.selectedDay.getTime())
    const providerIdsToday: number[] = offersForDay.map(o => o.provider)
    return this.lunchStore.providers
                              .filter(p => providerIdsToday.includes(p.id))
                              .filter(p => p.location === this.lunchStore.selectedLocation.name)
                              .sort((provider1, provider2) => provider1.name.localeCompare(provider2.name))
  }
}
</script>
