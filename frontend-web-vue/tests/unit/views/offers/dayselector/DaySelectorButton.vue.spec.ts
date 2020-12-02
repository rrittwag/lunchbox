/* eslint-disable @typescript-eslint/no-non-null-assertion */
import DaySelectorButton from '@/views/offers/dayselector/DaySelectorButton.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('DaySelectorButton', () => {
  it('renders snapshot for previous day', () => {
    const wrapper = mountUnit(DaySelectorButton, {
      direction: 'prev',
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('renders snapshot for next day', () => {
    const wrapper = mountUnit(DaySelectorButton, {
      direction: 'next',
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('has no `active` style when disabled', () => {
    const wrapper = mountUnit(DaySelectorButton, {
      direction: 'prev',
      disabled: true,
    })

    // Trotz disabled-Zustand kann ein Button bei Klick active werden.
    // -> https://stackoverflow.com/a/12592035
    // Für diesen unschönen Fall unterbinden wir das Stylen des Buttons.
    expect(wrapper.classes().find(c => c.startsWith('active'))).toBeUndefined()
  })

  it('emits click event', () => {
    const wrapper = mountUnit(DaySelectorButton, {
      direction: 'prev',
    })

    wrapper.find('button').trigger('click')

    expect(wrapper.emitted().click).toBeDefined()
    expect(wrapper.emitted().click!.length).toBe(1)
  })
})
