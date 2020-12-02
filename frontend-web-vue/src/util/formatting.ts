export function formatEuro(priceInCent?: number): string {
  if (!priceInCent) return ''
  const euroString = `${Math.floor(priceInCent / 100)}`
  const centString = `0${priceInCent % 100}`.slice(-2)
  return `${euroString},${centString}`
}

export function formatToWeekday(date?: Date): string {
  if (!date) return ''
  const options = { weekday: 'long' }
  return date.toLocaleDateString(undefined, options)
}

export function formatToLocalDate(date?: Date): string {
  if (!date) return ''
  return date.toLocaleDateString()
}

export function formatToISODate(date?: Date): string {
  if (!date) return ''
  return date.toISOString().substring(0, 10)
}
