<script setup lang="ts">
import type { LunchOffer, LunchProvider } from '@/model/lunch'
import { storeToRefs } from 'pinia'
import { computed } from 'vue'
import { useLunchStore } from '@/store/lunch'
import OfferBox from '@/views/offers/OfferBox.vue'

const store = useLunchStore()
const { providers, offers, selectedDay, selectedLocation } = storeToRefs(store)

const visibleOffers = computed<LunchOffer[]>(() => {
  const providerIDsForSelectedLocation: number[] = providers.value
    .filter(p => p.location === selectedLocation.value.name)
    .map(p => p.id)
  return offers.value
    .filter(o => providerIDsForSelectedLocation.includes(o.provider))
    .filter(o => new Date(o.day).getTime() === selectedDay.value.getTime()) as LunchOffer[]
})

function visibleOffersOf(provider: LunchProvider): LunchOffer[] {
  return visibleOffers.value.filter(o => o.provider === provider.id)
}

const visibleProviders = computed<LunchProvider[]>(() => {
  const providerIDs: number[] = visibleOffers.value.map(o => o.provider)
  return providers.value
    .filter(p => providerIDs.includes(p.id))
    .sort((provider1, provider2) => provider1.name.localeCompare(provider2.name))
})
</script>

<template>
  <div class="flex flex-wrap content-start">
    <div
      v-for="provider in visibleProviders"
      :key="provider.id"
      class="w-full px-2 pb-4 sm:w-1/2 sm:max-w-md lg:w-1/3 xl:w-1/4 xl:px-4"
    >
      <OfferBox :provider="provider" :offers="visibleOffersOf(provider)" />
    </div>
  </div>
</template>
