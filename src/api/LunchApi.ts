import { AxiosPromise } from 'axios'
import http from './http'
import { LunchOffer, LunchProvider } from '@/model'

export class LunchApi {
  getOffers(): Promise<LunchOffer[]> {
    const promise: AxiosPromise = http.get('api/v2/lunchOffer')
    return promise.then(response => response.data)
  }

  getProviders(): Promise<LunchProvider[]> {
    const promise: AxiosPromise = http.get('api/v2/lunchProvider')
    return promise.then(response => response.data)
  }
}
