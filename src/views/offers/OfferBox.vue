<template>
  <div
    class="flex flex-col items-center
           px-4 py-2 sm:pt-4 sm:pb-6
           shadow-xl bg-white rounded-lg
           border-l-8 sm:border-t-8 sm:border-l-0 border-primary-400"
  >
    <span class="text-3xl sm:text-4xl font-serif text-neutral-800">
      {{ provider.name }}
    </span>
    <Offer v-for="offer in offers" :key="offer.id" :offer="offer" />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { LunchOffer, LunchProvider } from '@/model'
import Offer from './Offer.vue'

@Component({
  components: {
    Offer,
  },
})
export default class OfferBox extends Vue {
  @Prop() provider!: LunchProvider

  @Prop() offers!: LunchOffer[]
}
</script>
