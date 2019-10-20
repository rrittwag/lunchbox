import { Module, VuexModule, Action, Mutation } from 'vuex-module-decorators'
import { store, LoadingState } from '@/store'
import { LunchOffer, LunchProvider, LunchLocation } from '@/model'
import { LunchApi } from '@/api'

// --- mutations ---

const SET_LOADING_STATE = 'SET_LOADING_STATE'
const SET_PROVIDERS = 'SET_PROVIDERS'
const SET_OFFERS = 'SET_OFFERS'
const SET_SELECTED_DAY = 'SET_SELECTED_DAY'
const SET_SELECTED_LOCATION = 'SET_SELECTED_LOCATION'

// --- module ---

@Module({ store, dynamic: true, namespaced: true, name: 'lunch' })
export class LunchStore extends VuexModule {

  // manual injection: Vue injects into components, but not into store/modules
  api: LunchApi = new LunchApi()

  // --- providers ---

  providers: LunchProvider[] = []

  @Mutation
  protected [SET_PROVIDERS](providers: LunchProvider[]) {
    this.providers = providers
  }

  // --- offers ---

  offers: LunchOffer[] = []

  @Mutation
  protected [SET_OFFERS](offers: LunchOffer[]) {
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
    locationName = locationName.replace(/"/g, '')

    const filteredLocation = this.locations.filter(l => l.name === locationName)
    if (filteredLocation.length > 0)
      return filteredLocation[0]
    return this.locations[0]
  }

  @Mutation
  protected [SET_SELECTED_LOCATION](location: LunchLocation) {
    this.selectedLocation = location
    let locationName = ''
    if (location)
      locationName = location.name
    localStorage.setItem('lunchboxWebapp.STORAGEKEY_LOCATION', locationName)
  }

  @Action
  setSelectedLocation(location: LunchLocation) {
    this.context.commit(SET_SELECTED_LOCATION, location)
  }

  // --- selected day ---

  selectedDay: Date = this.today()

  private today(): Date {
    const localNow = new Date()
    return new Date(Date.UTC(localNow.getFullYear(), localNow.getMonth(), localNow.getDate()))
  }

  @Mutation
  protected [SET_SELECTED_DAY](selectedDay: Date) {
    this.selectedDay = selectedDay
  }

  @Action
  setSelectedDay(selectedDay: Date) {
    this.context.commit(SET_SELECTED_DAY, selectedDay)
  }

  // --- api call ---

  loadingState: LoadingState = LoadingState.NotStarted

  @Mutation
  protected [SET_LOADING_STATE](loadingState: LoadingState) {
    this.loadingState = loadingState
  }

  @Action
  async loadFromApi() {
    this.context.commit(SET_LOADING_STATE, LoadingState.Loading)

    try {
      const [providers, offers] =
        await Promise.all([this.api.getProviders(), this.api.getOffers()])

      this.context.commit(SET_PROVIDERS, providers)
      this.context.commit(SET_OFFERS, offers)

      this.context.commit(SET_LOADING_STATE, LoadingState.Done)

    } catch (error) {
      this.context.commit(SET_LOADING_STATE, LoadingState.Failed)
    }
  }
}
