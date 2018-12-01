import { Module, VuexModule, Action, Mutation } from 'vuex-module-decorators'
import Axios, { AxiosResponse } from 'axios'
import store from '@/store/'
import LunchLocation from '@/model/LunchLocation'
import LunchProvider from '@/model/LunchProvider'
import LunchOffer from '@/model/LunchOffer'

@Module({ store, dynamic: true, name: 'lunch' })
export default class LunchStoreModule extends VuexModule {

  // --- providers ---

  providers: LunchProvider[] = []

  @Mutation
  mutateProviders(providers: LunchProvider[]) {
    this.providers = providers
  }

  @Action
  async fetchProviders() {
    const response: AxiosResponse = await Axios.get('api/v1/lunchProvider')
    // TODO: error handling!
    this.context.commit('mutateProviders', response.data)
  }

  providersByLocation(location: LunchLocation): LunchProvider[] {
    return this.providers
                    .filter(p => p.location === location.name)
  }

  // --- offers ---

  offers: LunchOffer[] = []

  @Mutation
  mutateOffers(offers: LunchOffer[]) {
    this.offers = offers
  }

  @Action
  async fetchOffers() {
    const response: AxiosResponse = await Axios.get('api/v1/lunchOffer')
    // TODO: error handling!
    this.context.commit('mutateOffers', response.data)
  }

  offersByDay(day: Date): LunchOffer[] {
    return this.offers
                    .filter(p => new Date(p.day) === day)
  }

  // --- locations ---

  locations: LunchLocation[] = [
    new LunchLocation('Neubrandenburg', 'NB'),
    new LunchLocation('Berlin Springpfuhl', 'B'),
  ]

  selectedLocation: LunchLocation = this.locations[0] // TODO: load from local storage

  @Mutation
  mutateSelectedLocation(location: LunchLocation) {
    this.selectedLocation = location
     // TODO: save to local storage
  }

  // --- selected day ---

  selectedDay: Date = new Date('2018-12-03')

}
