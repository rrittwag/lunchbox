<template>
  <div class="offer-box-group flex flex-wrap items-start">
    <div
      v-for="provider in visibleProviders"
      :key="provider.id"
      class="w-full sm:w-1/2 lg:w-1/3 xl:w-1/4
             sm:max-w-md
             pb-4 px-2 xl:px-4"
    >
      <OfferBox :provider="provider" :offers="visibleOffersOf(provider)" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import { LunchStore } from '@/store/modules/LunchStore'
import OfferBox from '@/views/offers/OfferBox.vue'
import { LunchOffer } from '@/model/LunchOffer'
import { LunchProvider } from '@/model/LunchProvider'

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
