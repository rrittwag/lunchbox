import { LunchOffer, LunchProvider } from '@/model'
import { fetchWithTimeout } from '@/api/http'

export class LunchApi {
  getOffers(): Promise<LunchOffer[]> {
    return fetchWithTimeout('api/v2/lunchOffer').then(response => response.json())
  }

  getProviders(): Promise<LunchProvider[]> {
    return fetchWithTimeout('api/v2/lunchProvider').then(response => response.json())
  }
}
