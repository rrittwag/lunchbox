<script setup lang="ts">
import type { LunchOffer, LunchProvider } from '@/model/lunch'
import Offer from '@/views/offers/Offer.vue'
import { ref } from 'vue'

const props = defineProps<{
  provider: LunchProvider
  offers: LunchOffer[]
}>()

const showDetails = ref(false)
const onClick = () => (showDetails.value = !showDetails.value)
</script>

<template>
  <article
    class="flex flex-col items-center rounded-lg border-l-8 border-primary-500 bg-card px-4 pb-3 pt-2 shadow-xl sm:border-l-0 sm:border-t-8 sm:pb-6 sm:pt-4"
    @click="onClick"
  >
    <h3 class="font-display text-3xl text-neutral-700 sm:text-4xl">
      {{ props.provider.name }}
    </h3>
    <ul v-if="props.offers.length > 0" class="w-full list-none">
      <Offer v-for="offer in props.offers" :key="offer.id" :offer="offer" :show-details-in-x-s="showDetails" />
    </ul>
  </article>
</template>
