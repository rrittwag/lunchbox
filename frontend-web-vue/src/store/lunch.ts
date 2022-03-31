import { ref, readonly } from 'vue'
import api from '@/api/lunch'
import { ApiError } from '@/api/http'
import { today } from '@/util/date'
import { defineStore } from 'pinia'
import { LunchLocation, LunchOffer, LunchProvider } from '@/model/lunch'
import { useStorage } from '@vueuse/core'

export const useLunchStore = defineStore('lunch', () => {
  // --------------------
  //  offers & providers
  // --------------------
  const offers = useStorage<LunchOffer[]>('offers', [])
  const providers = useStorage<LunchProvider[]>('providers', [])
  const isLoading = ref(false)
  const error = ref<ApiError | undefined>()

  async function loadFromApi() {
    isLoading.value = true
    try {
      const providersPromise = api.getProviders()
      const offersPromise = api.getOffers()
      providers.value = await providersPromise
      offers.value = await offersPromise
    } catch (e) {
      error.value = e as ApiError
    }
    isLoading.value = false
  }

  // -------------------
  //  locations
  // -------------------
  const locations: LunchLocation[] = [
    new LunchLocation('Neubrandenburg', 'NB'),
    new LunchLocation('Berlin Springpfuhl', 'B'),
  ]
  const selectedLocation = ref(loadSelectedLocationFromLocalStorage())

  function loadSelectedLocationFromLocalStorage(): LunchLocation {
    let locationName: string | null = localStorage.getItem('lunchboxWebapp.STORAGEKEY_LOCATION')
    if (!locationName || locationName === '') return locations[0]

    // die alte Angular-App speicherte den Wert mit ""
    locationName = locationName.replace(/"/g, '')

    const filteredLocation = locations.filter((l) => l.name === locationName)
    if (filteredLocation.length > 0) return filteredLocation[0]
    return locations[0]
  }

  function selectLocation(newLocation: LunchLocation) {
    selectedLocation.value = newLocation
    let locationName = ''
    if (newLocation) locationName = newLocation.name
    localStorage.setItem('lunchboxWebapp.STORAGEKEY_LOCATION', locationName)
  }

  // ------------------
  //  selected day
  // ------------------
  const selectedDay = ref(today())

  function selectDay(newSelectedDay: Date) {
    selectedDay.value = newSelectedDay
  }

  // ---------------------
  //  export
  // ---------------------
  return {
    offers: readonly(offers),
    providers: readonly(providers),
    isLoading: readonly(isLoading),
    error: readonly(error),
    loadFromApi,
    locations: readonly(locations),
    selectedLocation,
    selectLocation,
    selectedDay,
    selectDay,
  }
})
