<template>
  <b-list-group>
    <b-list-group-item variant='primary'>
      <b>{{ provider.name }}</b>
    </b-list-group-item>
    <b-list-group-item v-for='offer in offers' :key='offer.id'>
      <b-row>
        <b-col cols='9' class='offer-name'>{{ offer.name }}</b-col>
        <b-col cols='3' class='offer-price'>{{ offer.price | formatEuro }}</b-col>
      </b-row>
    </b-list-group-item>
  </b-list-group>
</template>



<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { LunchOffer, LunchProvider } from '@/model'

@Component
export default class OfferBox extends Vue {
  @Prop() private provider!: LunchProvider

  @Prop() private offers!: LunchOffer[]
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
