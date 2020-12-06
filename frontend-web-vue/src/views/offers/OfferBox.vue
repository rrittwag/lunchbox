<template>
  <article
    class="flex flex-col items-center
           px-4 pt-2 pb-3 sm:pt-4 sm:pb-6
           shadow-xl bg-card rounded-lg
           border-l-8 sm:border-l-0 sm:border-t-8 border-primary-500"
    @click="onClick"
  >
    <h3 class="text-3xl sm:text-4xl font-display text-neutral-700">
      {{ props.provider.name }}
    </h3>
    <ul class="w-full list-none">
      <Offer
        v-for="offer in props.offers"
        :key="offer.id"
        :offer="offer"
        :showDetailsInXS="showDetails"
      />
    </ul>
  </article>
</template>

<script setup lang="ts">
import { defineProps, ref } from 'vue'
import Offer from '/@/views/offers/Offer.vue'
import { LunchProvider } from '/@/model/LunchProvider'
import { LunchOffer } from '/@/model/LunchOffer'

const props = defineProps<{
  provider: LunchProvider
  offers: LunchOffer[]
}>()

const showDetails = ref(false)
const onClick = () => (showDetails.value = !showDetails.value)
</script>
