import DaySelectorButton from '@/views/offers/dayselector/DaySelectorButton.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('DaySelectorButton', () => {
  it('renders snapshot for previous day', () => {
    const wrapper = mountUnit(DaySelectorButton, {
      direction: 'prev',
      disabled: false,
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('renders snapshot for next day', () => {
    const wrapper = mountUnit(DaySelectorButton, {
      direction: 'next',
      disabled: false,
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('has no active state when disabled', () => {
    const wrapper = mountUnit(DaySelectorButton, {
      direction: 'prev',
      disabled: true,
    })

    expect(wrapper.classes().find(c => c.startsWith('active'))).toBeFalsy()
  })
})
