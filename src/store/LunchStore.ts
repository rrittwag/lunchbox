import { Inject } from 'vue-property-decorator'
import { Module, VuexModule, Action, Mutation } from 'vuex-module-decorators'
import { AxiosResponse, AxiosPromise } from 'axios'
import { store, LoadingState } from '@/store'
import { LunchOffer, LunchProvider, LunchLocation } from '@/model'
import { LunchApi } from '@/api'

@Module({ store, dynamic: true, name: 'lunch' })
export class LunchStore extends VuexModule {

  @Inject() api: LunchApi = new LunchApi() // bad: Vue injects into components, but not into store/modules!

  // --- providers ---

  providers: LunchProvider[] = []

  @Mutation
  mutateProviders(providers: LunchProvider[]) {
    this.providers = providers
  }

  // --- offers ---

  offers: LunchOffer[] = []

  @Mutation
  mutateOffers(offers: LunchOffer[]) {
    this.offers = offers
  }

  // --- locations ---

  locations: LunchLocation[] = [
    new LunchLocation('Neubrandenburg', 'NB'),
    new LunchLocation('Berlin Springpfuhl', 'B'),
  ]

  selectedLocation: LunchLocation = this.loadSelectedLocationFromLocalStorage()

  loadSelectedLocationFromLocalStorage(): LunchLocation {
    let locationName: string | null = localStorage.getItem('lunchboxWebapp.STORAGEKEY_LOCATION')
    if (locationName === null || locationName.length === 0)
      return this.locations[0]
    locationName = locationName.replace(/\"/g, '') // die alte Angular-App speicherte den Wert mit ""
    const filteredLocation = this.locations.filter(l => l.name === locationName)
    if (filteredLocation.length > 0)
      return filteredLocation[0]
    return this.locations[0]
  }

  @Mutation
  mutateSelectedLocation(location: LunchLocation) {
    this.selectedLocation = location
    let locationName: string = ''
    if (!!location)
      locationName = location.name
    localStorage.setItem('lunchboxWebapp.STORAGEKEY_LOCATION', locationName)
  }

  // --- selected day ---

  selectedDay: Date = this.today()

  private today(): Date {
    const localNow = new Date()
    return new Date(Date.UTC(localNow.getFullYear(), localNow.getMonth(), localNow.getDate()))
  }

  @Mutation
  mutateSelectedDay(selectedDay: Date) {
    this.selectedDay = selectedDay
  }

  // --- api call ---

  loadingState: LoadingState = LoadingState.NotStarted

  @Mutation
  mutateLoadingState(loadingState: LoadingState) {
    this.loadingState = loadingState
  }

  @Action
  async loadFromApi() {
    try {
      this.context.commit('mutateLoadingState', LoadingState.Loading)

      const providerPromise: AxiosPromise = this.api.getProviders()
      const offerPromise: AxiosPromise = this.api.getOffers()

      const providerResponse: AxiosResponse = await providerPromise
      const offerResponse: AxiosResponse = await offerPromise

      if (providerResponse.status !== 200 || offerResponse.status !== 200) {
        throw new Error('Response code must be 200 in \n' + JSON.stringify(providerResponse) + 'and in \n' + JSON.stringify(offerResponse))
      }

      this.context.commit('mutateProviders', providerResponse.data)
      this.context.commit('mutateOffers', offerResponse.data)

      this.context.commit('mutateLoadingState', LoadingState.Done)
    } catch (error) {
      console.error(error)
      this.context.commit('mutateLoadingState', LoadingState.Failed)
    }
  }
}
