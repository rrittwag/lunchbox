import { formatToWeekday, formatToLocalDate, formatEuro, formatToISODate } from '@/util/formatting'
import { describe, test, expect } from 'vitest'

describe('formatEuro', () => {
  test('WHEN undefined  THEN empty string', () => {
    expect(formatEuro(undefined)).toEqual('')
  })

  test('WHEN priceInCent = 1  THEN 0,01', () => {
    expect(formatEuro(1)).toEqual('0,01')
  })

  test('WHEN priceInCent = 123  THEN return 1,23', () => {
    expect(formatEuro(123)).toEqual('1,23')
  })

  test('WHEN priceInCent = 1234  THEN return 12,34', () => {
    expect(formatEuro(1234)).toEqual('12,34')
  })
})

describe('formatToWeekday', () => {
  test('WHEN undefined  THEN empty string', () => {
    expect(formatToWeekday(undefined)).toEqual('')
  })

  test('WHEN 2018-03-27  THEN Mittwoch', () => {
    const result = formatToWeekday(DATE_2018_03_27)
    expect(result).toEqual('Dienstag')
  })
})

describe('formatToLocalDate', () => {
  test('WHEN undefined  THEN empty string', () => {
    expect(formatToLocalDate(undefined)).toEqual('')
  })

  test('WHEN 2018-03-27  THEN locale date format', () => {
    const result = formatToLocalDate(DATE_2018_03_27)
    expect(result).toEqual('27.3.2018') // LANG=de_DE.UTF-8
  })
})

describe('formatToISODate', () => {
  test('WHEN undefined  THEN empty string', () => {
    expect(formatToISODate(undefined)).toEqual('')
  })

  test('WHEN 2018-03-27  THEN iso date string', () => {
    const result = formatToISODate(DATE_2018_03_27)
    expect(result).toEqual('2018-03-27')
  })
})

// mocks 'n' stuff

const DATE_2018_03_27 = new Date('2018-03-27')
