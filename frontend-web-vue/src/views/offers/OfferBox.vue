<template>
  <article
    class="flex flex-col items-center rounded-lg border-l-8 border-primary-500 bg-card px-4 pt-2 pb-3 shadow-xl sm:border-l-0 sm:border-t-8 sm:pt-4 sm:pb-6"
    @click="onClick"
  >
    <h3 class="font-display text-3xl text-neutral-700 sm:text-4xl">
      {{ props.provider.name }}
    </h3>
    <ul class="w-full list-none">
      <Offer v-for="offer in props.offers" :key="offer.id" :offer="offer" :showDetailsInXS="showDetails" />
    </ul>
  </article>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Offer from '@/views/offers/Offer.vue'
import { LunchOffer, LunchProvider } from '@/model/lunch'

const props = defineProps<{
  provider: LunchProvider
  offers: LunchOffer[]
}>()

const showDetails = ref(false)
const onClick = () => (showDetails.value = !showDetails.value)
</script>
