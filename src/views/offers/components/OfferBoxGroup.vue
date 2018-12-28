<template>
  <b-container fluid>
      <b-row>
        <b-col
          sm='6' md='4' lg='3'
          v-for='provider in visibleProviders'
          :key='provider.id'
        >
          <OfferBox
            :provider='provider'
            :offers='visibleOffersOf(provider)'
          />
        </b-col>
      </b-row>
  </b-container>
</template>



<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import { LunchStore } from '@/store'
import { LunchProvider, LunchOffer } from '@/model'
import OfferBox from './OfferBox.vue'

@Component({
  components: {
    OfferBox,
  },
})
export default class OfferBoxGroup extends Vue {
  @Inject() lunchStore!: LunchStore

  get visibleOffers(): LunchOffer[] {
    const providerIDsForSelectedLocation: number[] = this.lunchStore.providers
                              .filter(p => p.location === this.lunchStore.selectedLocation.name)
                              .map(p => p.id)
    return this.lunchStore.offers
                  .filter(o => providerIDsForSelectedLocation.includes(o.provider))
                  .filter(o => new Date(o.day).getTime() === this.lunchStore.selectedDay.getTime())
  }

  visibleOffersOf(provider: LunchProvider): LunchOffer[] {
    return this.visibleOffers.filter(o => o.provider === provider.id)
  }

  get visibleProviders(): LunchProvider[] {
    const providerIDs: number[] = this.visibleOffers.map(o => o.provider)
    return this.lunchStore.providers
                              .filter(p => providerIDs.includes(p.id))
                              .sort((provider1, provider2) => provider1.name.localeCompare(provider2.name))
  }
}
</script>
