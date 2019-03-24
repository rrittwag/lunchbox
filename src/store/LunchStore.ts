import { Inject } from 'vue-property-decorator'
import { Module, VuexModule, Action, Mutation } from 'vuex-module-decorators'
import { store, LoadingState } from '@/store'
import { LunchOffer, LunchProvider, LunchLocation } from '@/model'
import { LunchApi } from '@/api'

@Module({ store, dynamic: true, namespaced: true, name: 'lunch' })
export class LunchStore extends VuexModule {

  // manual injection: Vue injects into components, but not into store/modules
  @Inject() api: LunchApi = new LunchApi()

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

  private loadSelectedLocationFromLocalStorage(): LunchLocation {
    let locationName: string | null = localStorage.getItem('lunchboxWebapp.STORAGEKEY_LOCATION')
    if (!locationName || locationName === '')
      return this.locations[0]

    // die alte Angular-App speicherte den Wert mit ""
    locationName = locationName.replace(/\"/g, '')

    const filteredLocation = this.locations.filter(l => l.name === locationName)
    if (filteredLocation.length > 0)
      return filteredLocation[0]
    return this.locations[0]
  }

  @Mutation
  mutateSelectedLocation(location: LunchLocation) {
    this.selectedLocation = location
    let locationName = ''
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
    this.context.commit('mutateLoadingState', LoadingState.Loading)

    try {
      const [providers, offers] =
                await Promise.all([this.api.getProviders(), this.api.getOffers()])

      this.context.commit('mutateProviders', providers)
      this.context.commit('mutateOffers', offers)

      this.context.commit('mutateLoadingState', LoadingState.Done)

    } catch (error) {
      this.context.commit('mutateLoadingState', LoadingState.Failed)
    }
  }
}
