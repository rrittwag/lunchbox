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

  it('has no active state when disabled', () => {
    const wrapper = mountUnit(DaySelectorButton, {
      direction: 'prev',
      disabled: true,
    })

    // Die Spec ist uneindeutig: Ein disabled Button kann bei Klick den Zustand active annehmen
    expect(wrapper.classes().find(c => c.startsWith('active'))).toBeFalsy()
  })

  it('emits click event', () => {
    const wrapper = mountUnit(DaySelectorButton, {
      direction: 'prev',
    })

    wrapper.find('button').trigger('click')

    expect(wrapper.emitted().click).toBeDefined()
    expect(wrapper.emitted().click?.length).toBe(1)
  })
})
