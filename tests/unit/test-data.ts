import { LunchOffer, LunchProvider } from '@/model'

// --- test providers ---

export const mensa: LunchProvider = {
  id: 1,
  name: 'Mensa',
  location: 'Berlin',
}

// --- test offers ---

export const gyros: LunchOffer = {
  id: 1,
  name: 'Gyros',
  provider: 1,
  price: 350,
  day: '2018-01-31',
}

export const soljanka: LunchOffer = {
  id: 2,
  name: 'Soljanka',
  provider: 1,
  price: 250,
  day: '2018-01-31',
}
