/* eslint-disable @typescript-eslint/no-non-null-assertion */
import DaySelector from '@/views/offers/DaySelector.vue'
import { DaySelectorDirection } from '@/views/offers/dayselector/DaySelectorDirection'
import { mount } from '@vue/test-utils'

describe('DaySelector', () => {
  it('renders snapshot', () => {
    const wrapper = mount(DaySelector, {
      props: { selectedDay: YESTERDAY },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('has disabled previous button', () => {
    const wrapper = mount(DaySelector, {
      props: { selectedDay: YESTERDAY, disabledPrev: true },
    })

    const buttons = wrapper.findAll('button')
    expect(buttons[0].attributes()).toHaveProperty('disabled')
    expect(buttons[1].attributes()).not.toHaveProperty('disabled')
  })

  it('has disabled next button', () => {
    const wrapper = mount(DaySelector, {
      props: { selectedDay: TOMORROW, disabledNext: true },
    })

    const buttons = wrapper.findAll('button')
    expect(buttons[0].attributes()).not.toHaveProperty('disabled')
    expect(buttons[1].attributes()).toHaveProperty('disabled')
  })

  it('emits event WHEN click previous day', async () => {
    const wrapper = mount(DaySelector, {
      props: { selectedDay: TODAY },
    })

    await wrapper.findAll('button')[0].trigger('click')

    expect(wrapper.emitted().change).toBeDefined()
    expect(wrapper.emitted().change!.length).toBe(1)
    expect(wrapper.emitted().change![0]).toEqual([DaySelectorDirection.PREVIOUS])
  })

  it('emits event WHEN click next day', () => {
    const wrapper = mount(DaySelector, {
      props: { selectedDay: TODAY },
    })

    wrapper.findAll('button')[1].trigger('click')

    expect(wrapper.emitted().change).toBeDefined()
    expect(wrapper.emitted().change!.length).toBe(1)
    expect(wrapper.emitted().change![0]).toEqual([DaySelectorDirection.NEXT])
  })

  it('emits event WHEN pressing left arrow key', () => {
    const wrapper = mount(DaySelector, {
      props: { days: [YESTERDAY, TODAY, TOMORROW], selectedDay: TODAY },
    })

    window.dispatchEvent(new KeyboardEvent('keydown', { key: 'ArrowLeft' }))

    expect(wrapper.emitted().change).toBeDefined()
    expect(wrapper.emitted().change!.length).toBe(1)
    expect(wrapper.emitted().change![0]).toEqual([DaySelectorDirection.PREVIOUS])
  })

  it('emits event WHEN pressing right arrow key', () => {
    const wrapper = mount(DaySelector, {
      props: { days: [YESTERDAY, TODAY, TOMORROW], selectedDay: TODAY },
    })

    window.dispatchEvent(new KeyboardEvent('keydown', { key: 'ArrowRight' }))

    expect(wrapper.emitted().change).toBeDefined()
    expect(wrapper.emitted().change!.length).toBe(1)
    expect(wrapper.emitted().change![0]).toEqual([DaySelectorDirection.NEXT])
  })
})

// mocks 'n' stuff

const YESTERDAY = new Date('2019-12-01')
const TODAY = new Date('2019-12-02')
const TOMORROW = new Date('2019-12-03')
