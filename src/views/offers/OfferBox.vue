<template>
  <b-list-group class="offer-box">
    <b-list-group-item variant="primary" class="text-center font-weight-bold">
      {{ provider.name }}
    </b-list-group-item>

    <b-list-group-item
      v-for="offer in offers"
      :key="offer.id"
      class="d-flex justify-content-between"
    >
      <span class="offer-name">
        {{ offer.name }}
      </span>
      <span
        class="offer-price
                   text-right text-nowrap pl-2"
      >
        {{ offer.price | formatEuro }}
      </span>
    </b-list-group-item>
  </b-list-group>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { LunchOffer, LunchProvider } from '@/model'

@Component
export default class OfferBox extends Vue {
  @Prop() provider!: LunchProvider

  @Prop() offers!: LunchOffer[]
}
</script>
