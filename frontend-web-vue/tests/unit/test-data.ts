import { LunchProvider } from '@/model/LunchProvider'
import { LunchOffer } from '@/model/LunchOffer'
import { LunchLocation } from '@/model/LunchLocation'
import { Theme } from '@/store/theme'

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

// --- test themes ---

export const themeRed: Theme = { cssClass: 'theme-red', label: 'Red' }
export const themeGreen: Theme = { cssClass: 'theme-green', label: 'Green' }
export const themeBlue: Theme = { cssClass: 'theme-blue', label: 'Blue' }
