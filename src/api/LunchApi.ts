import Axios, { AxiosPromise } from 'axios'
import { LunchOffer, LunchProvider } from '@/model'

export default class LunchApi {

  getOffers(): AxiosPromise<LunchOffer[]> {
    return Axios.get('api/v1/lunchOffer')
  }

  getProviders(): AxiosPromise<LunchProvider[]> {
    return Axios.get('api/v1/lunchProvider')
  }
}
