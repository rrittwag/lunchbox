import type { LunchLocation, LunchOffer, LunchProvider } from '@/model/lunch'

// --- test providers ---

export const mensa: LunchProvider = {
  id: 1,
  name: 'Mensa',
  location: 'Berlin',
  url: 'https://mensa.berlin',
}

// --- test offers ---

export const gyros: LunchOffer = {
  id: 1,
  name: 'Gyros',
  description: 'mit Pommes',
  provider: 1,
  price: 350,
  day: '2018-01-31',
  tags: ['Tagesgericht', 'lecker'],
}

export const soljanka: LunchOffer = {
  id: 2,
  name: 'Soljanka',
  description: 'mit Paprika, Speck, Würstchen und sonstigen Resten',
  provider: 1,
  price: 250,
  day: '2018-01-31',
  tags: ['Tagessuppe'],
}

// --- test locations ---

export const hamburg: LunchLocation = {
  name: 'Hamburg',
  shortName: 'HH',
}

export const münchen: LunchLocation = {
  name: 'München',
  shortName: 'M',
}

// --- date/time ---

export const YESTERDAY = new Date('2019-12-01')
export const TODAY = new Date('2019-12-02')
export const TOMORROW = new Date('2019-12-03')
