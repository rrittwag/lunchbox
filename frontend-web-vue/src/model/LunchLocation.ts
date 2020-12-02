export class LunchLocation {
  readonly name: string
  readonly shortName: string

  constructor(name: string, shortName: string) {
    this.name = name
    this.shortName = shortName
  }
}
