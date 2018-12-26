<template>
  <b-list-group>
    <b-list-group-item variant='primary'><b>{{ provider.name }}</b></b-list-group-item>
    <b-list-group-item v-for='offer in visibleOffers()' :key='offer.id'>
      <b-row>
        <b-col cols='9' class='offer-name'>{{ offer.name }}</b-col>
        <b-col cols='3' class='offer-price'>{{ offer.price | formatEuro }}</b-col>
      </b-row>
    </b-list-group-item>
  </b-list-group>
</template>



<script lang="ts">
import { Component, Prop, Vue, Inject } from 'vue-property-decorator'
import { LunchStore } from '@/store'
import { LunchOffer, LunchProvider } from '@/model'

@Component
export default class OffersOfProvider extends Vue {
  @Inject() lunchStore!: LunchStore

  @Prop() private provider!: LunchProvider

  visibleOffers(): LunchOffer[] {
    return this.lunchStore.offers
                    .filter(o => o.provider === this.provider.id &&
                            new Date(o.day).getTime() === this.lunchStore.selectedDay.getTime())
  }
}
</script>



<style lang="scss">
.list-group-item-primary {
  text-align: center;
}

.offer-name {
  padding-right: 8px;
}

.offer-price {
  text-align: right;
  white-space: nowrap;
  padding-left: 0;
}
</style>
