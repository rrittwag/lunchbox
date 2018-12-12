<template>
  <b-container fluid>
      <b-row>
        <b-col v-for='provider in visibleProviders()' :key='provider.id'>
          <OffersOfProvider :provider='provider' />
        </b-col>
      </b-row>
  </b-container>
<!--
    <div class="row">
       <div class="col-sm-6 col-md-4 col-lg-3" ng-repeat="prov in offers.model.providers | filterProvidersByOffers:offers.visibleOffers | orderBy:'name'">
        <div class="panel panel-info">
          <div class="panel-heading">
            <h3 class="panel-title">{{prov.name}}</h3>
          </div>

          <ul class="list-group">
            <li class="list-group-item" ng-repeat="offer in offers.visibleOffers | filterOffersByProvider:prov">
              <div class="row">
                <div class="col-xs-9 offer-name">{{offer.name}}</div>
                <div class="col-xs-3 offer-price">{{offer.price | formatEuro}}</div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
-->
</template>



<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import LunchStore from '@/store/LunchStore'
import OffersOfProvider from './OffersOfProvider.vue'
import LunchProvider from '@/model/LunchProvider'

@Component({
  components: {
    OffersOfProvider,
  },
})
export default class Offers extends Vue {
  @Inject() lunchStore!: LunchStore

  visibleProviders(): LunchProvider[] {
    return this.lunchStore.providers
                              .filter(p => p.location === this.lunchStore.selectedLocation.name)
  }
}
</script>
