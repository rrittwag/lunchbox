import { formatEuro, formatToISODate, formatToLocalDate, formatToWeekday } from '@/util/formatting'

const DATE_2018_03_27 = new Date('2018-03-27')

describe('formatEuro', () => {
  it('wHEN undefined  THEN empty string', () => {
    expect(formatEuro(undefined)).toEqual('')
  })

  it('wHEN priceInCent = 1  THEN 0,01', () => {
    expect(formatEuro(1)).toEqual('0,01')
  })

  it('wHEN priceInCent = 123  THEN return 1,23', () => {
    expect(formatEuro(123)).toEqual('1,23')
  })

  it('wHEN priceInCent = 1234  THEN return 12,34', () => {
    expect(formatEuro(1234)).toEqual('12,34')
  })
})

describe('formatToWeekday', () => {
  it('wHEN undefined  THEN empty string', () => {
    expect(formatToWeekday(undefined)).toEqual('')
  })

  it('wHEN 2018-03-27  THEN Mittwoch', () => {
    const result = formatToWeekday(DATE_2018_03_27)
    expect(result).toEqual('Dienstag')
  })
})

describe('formatToLocalDate', () => {
  it('wHEN undefined  THEN empty string', () => {
    expect(formatToLocalDate(undefined)).toEqual('')
  })

  it('wHEN 2018-03-27  THEN locale date format', () => {
    const result = formatToLocalDate(DATE_2018_03_27)
    expect(result).toEqual('27.3.2018') // LANG=de_DE.UTF-8
  })
})

describe('formatToISODate', () => {
  it('wHEN undefined  THEN empty string', () => {
    expect(formatToISODate(undefined)).toEqual('')
  })

  it('wHEN 2018-03-27  THEN iso date string', () => {
    const result = formatToISODate(DATE_2018_03_27)
    expect(result).toEqual('2018-03-27')
  })
})
