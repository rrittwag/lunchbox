<template>
  <b-list-group>
    <b-list-group-item variant="primary"><b>{{ provider.name }}</b></b-list-group-item>
    <b-list-group-item v-for='offer in visibleOffers()' v-bind:key='offer.id'>{{ offer.name }}</b-list-group-item>
  </b-list-group>
</template>



<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import LunchStore from '@/store/lunch'
import LunchProvider from '@/model/LunchProvider'
import LunchOffer from '@/model/LunchOffer'

@Component
export default class OffersOfProvider extends Vue {
  private lunchStore: LunchStore = getModule(LunchStore)

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
</style>
