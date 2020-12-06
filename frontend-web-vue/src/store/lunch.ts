import { computed, ref, Ref } from 'vue'
import api from '/@/api/LunchApi'
import { ApiError } from '/@/api/http'
import { LunchProvider } from '/@/model/LunchProvider'
import { LunchOffer } from '/@/model/LunchOffer'
import { today } from '/@/util/date'
import { LunchLocation } from '/@/model/LunchLocation'

const locations: LunchLocation[] = [
  new LunchLocation('Neubrandenburg', 'NB'),
  new LunchLocation('Berlin Springpfuhl', 'B'),
]

const offers: Ref<LunchOffer[]> = ref([])
const providers: Ref<LunchProvider[]> = ref([])
const isLoading: Ref<boolean> = ref(false)
const error: Ref<ApiError | null> = ref(null)

const selectedDay = ref(today())

const selectedLocation = ref(loadSelectedLocationFromLocalStorage())

function loadSelectedLocationFromLocalStorage(): LunchLocation {
  let locationName: string | null = localStorage.getItem('lunchboxWebapp.STORAGEKEY_LOCATION')
  if (!locationName || locationName === '') return locations[0]

  // die alte Angular-App speicherte den Wert mit ""
  locationName = locationName.replace(/"/g, '')

  const filteredLocation = locations.filter(l => l.name === locationName)
  if (filteredLocation.length > 0) return filteredLocation[0]
  return locations[0]
}

export function useLunchStore() {
  async function loadFromApi() {
    isLoading.value = true
    try {
      const providersPromise = api.getProviders()
      const offersPromise = api.getOffers()
      providers.value = await providersPromise
      offers.value = await offersPromise
    } catch (e) {
      error.value = e
    }
    isLoading.value = false
  }

  function selectDay(newSelectedDay: Date) {
    selectedDay.value = newSelectedDay
  }

  function selectLocation(newLocation: LunchLocation) {
    selectedLocation.value = newLocation
    let locationName = ''
    if (newLocation) locationName = newLocation.name
    localStorage.setItem('lunchboxWebapp.STORAGEKEY_LOCATION', locationName)
  }

  return {
    locations: computed(() => locations),
    offers: computed(() => offers.value),
    providers: computed(() => providers.value),
    isLoading: computed(() => isLoading.value),
    error: computed(() => error.value),
    loadFromApi,
    selectedDay: computed(() => selectedDay.value),
    selectDay,
    selectedLocation: computed(() => selectedLocation.value),
    selectLocation,
  }
}

// eslint-disable-next-line @typescript-eslint/camelcase
export function __reset__TEST_ONLY__() {
  offers.value = []
  providers.value = []
  isLoading.value = false
  error.value = null
  selectedDay.value = today()
  selectedLocation.value = locations[0]
}
