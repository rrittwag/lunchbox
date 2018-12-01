import Axios, { AxiosPromise } from 'axios'
import LunchOffer from '@/model/LunchOffer'
import LunchProvider from '@/model/LunchProvider'

export default class LunchApi {

  getOffers(): AxiosPromise<LunchOffer[]> {
    return Axios.get('api/v1/lunchOffer')
  }

  getProviders(): AxiosPromise<LunchProvider[]> {
    return Axios.get('api/v1/lunchProvider')
  }
}
