import { computed, ref } from 'vue'
import api from '@/api/LunchApi'
import { ApiError } from '@/api/http'
import { LunchProvider } from '@/model/LunchProvider'
import { LunchOffer } from '@/model/LunchOffer'
import { today } from '@/util/date'
import { LunchLocation } from '@/model/LunchLocation'

// --------------------
//  offers & providers
// --------------------
const offers = ref<LunchOffer[]>([])
const providers = ref<LunchProvider[]>([])
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
export function useLunchStore() {
  return {
    offers: computed(() => offers.value),
    providers: computed(() => providers.value),
    isLoading: computed(() => isLoading.value),
    error: computed(() => error.value),
    loadFromApi,
    locations: computed(() => locations),
    selectedLocation: computed(() => selectedLocation.value),
    selectLocation,
    selectedDay: computed(() => selectedDay.value),
    selectDay,
  }
}

export function __reset__TEST_ONLY__() {
  offers.value = []
  providers.value = []
  isLoading.value = false
  error.value = undefined
  selectedDay.value = today()
  selectedLocation.value = locations[0]
}
