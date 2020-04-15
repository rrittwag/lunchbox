/* eslint-disable @typescript-eslint/no-non-null-assertion */
import { mountWithChildren } from '@tests/unit/test-util'
import DaySelector from '@/views/offers/DaySelector.vue'
import { DaySelectorDirection } from '@/views/offers/dayselector/DaySelectorEvent'

describe('DaySelector', () => {
  it('renders snapshot', () => {
    const wrapper = mountWithChildren(DaySelector, {
      days: [YESTERDAY, TODAY, TOMORROW],
      selectedDay: YESTERDAY,
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('has disabled previous button', () => {
    const wrapper = mountWithChildren(DaySelector, {
      days: [YESTERDAY, TODAY, TOMORROW],
      selectedDay: YESTERDAY,
    })

    const buttons = wrapper.findAll('button')
    expect(buttons.at(0).attributes().disabled).toBeTruthy()
    expect(buttons.at(1).attributes().disabled).toBeFalsy()
  })

  it('has disabled next button', () => {
    const wrapper = mountWithChildren(DaySelector, {
      days: [YESTERDAY, TODAY, TOMORROW],
      selectedDay: TOMORROW,
    })

    const buttons = wrapper.findAll('button')
    expect(buttons.at(0).attributes().disabled).toBeFalsy()
    expect(buttons.at(1).attributes().disabled).toBeTruthy()
  })

  it('has no disabled button', () => {
    const wrapper = mountWithChildren(DaySelector, {
      days: [YESTERDAY, TODAY, TOMORROW],
      selectedDay: TODAY,
    })

    const buttons = wrapper.findAll('button')
    expect(buttons.at(0).attributes().disabled).toBeFalsy()
    expect(buttons.at(1).attributes().disabled).toBeFalsy()
  })

  it('emits event WHEN click previous day', () => {
    const wrapper = mountWithChildren(DaySelector, {
      days: [YESTERDAY, TODAY, TOMORROW],
      selectedDay: TODAY,
    })

    wrapper
      .findAll('button')
      .at(0)
      .trigger('click')

    expect(wrapper.emitted().change).toBeDefined()
    expect(wrapper.emitted().change!.length).toBe(1)
    expect(wrapper.emitted().change![0]).toEqual([
      {
        direction: DaySelectorDirection.PREVIOUS,
        day: YESTERDAY,
      },
    ])
  })

  it('emits event WHEN click next day', () => {
    const wrapper = mountWithChildren(DaySelector, {
      days: [YESTERDAY, TODAY, TOMORROW],
      selectedDay: TODAY,
    })

    wrapper
      .findAll('button')
      .at(1)
      .trigger('click')

    expect(wrapper.emitted().change).toBeDefined()
    expect(wrapper.emitted().change!.length).toBe(1)
    expect(wrapper.emitted().change![0]).toEqual([
      {
        direction: DaySelectorDirection.NEXT,
        day: TOMORROW,
      },
    ])
  })

  it('emits event WHEN pressing left arrow key', () => {
    const wrapper = mountWithChildren(DaySelector, {
      days: [YESTERDAY, TODAY, TOMORROW],
      selectedDay: TODAY,
    })

    window.dispatchEvent(new KeyboardEvent('keydown', { key: 'ArrowLeft' }))

    expect(wrapper.emitted().change).toBeDefined()
    expect(wrapper.emitted().change!.length).toBe(1)
    expect(wrapper.emitted().change![0]).toEqual([
      {
        direction: DaySelectorDirection.PREVIOUS,
        day: YESTERDAY,
      },
    ])
  })

  it('emits event WHEN pressing right arrow key', () => {
    const wrapper = mountWithChildren(DaySelector, {
      days: [YESTERDAY, TODAY, TOMORROW],
      selectedDay: TODAY,
    })

    window.dispatchEvent(new KeyboardEvent('keydown', { key: 'ArrowRight' }))

    expect(wrapper.emitted().change).toBeDefined()
    expect(wrapper.emitted().change!.length).toBe(1)
    expect(wrapper.emitted().change![0]).toEqual([
      {
        direction: DaySelectorDirection.NEXT,
        day: TOMORROW,
      },
    ])
  })
})

// mocks 'n' stuff

const YESTERDAY = new Date('2019-12-01')
const TODAY = new Date('2019-12-02')
const TOMORROW = new Date('2019-12-03')
