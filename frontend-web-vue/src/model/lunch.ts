export class LunchLocation {
  readonly name: string
  readonly shortName: string

  constructor(name: string, shortName: string) {
    this.name = name
    this.shortName = shortName
  }
}

export interface LunchProvider {
  id: number
  name: string
  location: string
  url: string
}

export interface LunchOffer {
  id: number
  name: string
  description: string
  day: string
  price?: number
  tags: string[]
  provider: number
}
