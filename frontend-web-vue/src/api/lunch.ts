import { fetchWithTimeout } from '@/api/http'
import { LunchOffer, LunchProvider } from '@/model/lunch'

const URL_API_V2 = '/api/v2'
export const URL_API_LUNCHOFFER = `${URL_API_V2}/lunchOffer`
export const URL_API_LUNCHPROVIDER = `${URL_API_V2}/lunchProvider`

export default {
  getOffers(): Promise<LunchOffer[]> {
    return fetchWithTimeout(URL_API_LUNCHOFFER).then((response) => response.json())
  },

  getProviders(): Promise<LunchProvider[]> {
    return fetchWithTimeout(URL_API_LUNCHPROVIDER).then((response) => response.json())
  },
}
