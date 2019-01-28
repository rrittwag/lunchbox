import Axios from 'axios'
import { LunchOffer, LunchProvider } from '@/model'

export class LunchApi {

  async getOffers(): Promise<LunchOffer[]> {
    return Axios.get('api/v1/lunchOffer')
                  .then(response => response.data)
  }

  async getProviders(): Promise<LunchProvider[]> {
    return Axios.get('api/v1/lunchProvider')
                  .then(response => response.data)
  }
}
