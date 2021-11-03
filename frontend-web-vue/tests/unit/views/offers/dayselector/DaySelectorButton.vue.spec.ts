import { mount } from '@vue/test-utils'
import DaySelectorButton from '@/views/offers/dayselector/DaySelectorButton.vue'
import { DaySelectorDirection } from '@/views/offers/dayselector/DaySelectorDirection'

describe('DaySelectorButton', () => {
  it('renders snapshot for previous day', () => {
    const wrapper = mount(DaySelectorButton, {
      props: { direction: DaySelectorDirection.PREVIOUS },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('renders snapshot for next day', () => {
    const wrapper = mount(DaySelectorButton, {
      props: { direction: DaySelectorDirection.NEXT },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('has no `active` style when disabled', () => {
    const wrapper = mount(DaySelectorButton, {
      props: { direction: DaySelectorDirection.PREVIOUS, disabled: true },
    })

    // Trotz disabled-Zustand kann ein Button bei Klick active werden.
    // -> https://stackoverflow.com/a/12592035
    // Für diesen unschönen Fall unterbinden wir das Stylen des Buttons.
    expect(wrapper.classes().find((c) => c.startsWith('active'))).toBeUndefined()
  })

  it('emits click event', async () => {
    const wrapper = mount(DaySelectorButton, {
      props: { direction: DaySelectorDirection.PREVIOUS },
    })

    await wrapper.find('button').trigger('click')

    expect(wrapper.emitted().click).toBeDefined()
    expect(wrapper.emitted().click!.length).toBe(1)
  })
})
