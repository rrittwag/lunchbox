/* eslint-disable @typescript-eslint/no-non-null-assertion */
import DaySelectorButton from '/@/views/offers/dayselector/DaySelectorButton.vue'
import { mount } from '@vue/test-utils'

describe('DaySelectorButton', () => {
  it('renders snapshot for previous day', () => {
    const wrapper = mount(DaySelectorButton, {
      props: { direction: 'prev' },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('renders snapshot for next day', () => {
    const wrapper = mount(DaySelectorButton, {
      props: { direction: 'next' },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('has no `active` style when disabled', () => {
    const wrapper = mount(DaySelectorButton, {
      props: { direction: 'prev', disabled: true },
    })

    // Trotz disabled-Zustand kann ein Button bei Klick active werden.
    // -> https://stackoverflow.com/a/12592035
    // Für diesen unschönen Fall unterbinden wir das Stylen des Buttons.
    expect(wrapper.classes().find(c => c.startsWith('active'))).toBeUndefined()
  })

  // TODO: click is not emitted!? - cause: experimental script setup?
  it.skip('emits click event', async () => {
    const wrapper = mount(DaySelectorButton, {
      props: { direction: 'prev' },
    })

    await wrapper.find('button').trigger('click')

    expect(wrapper.emitted().click).toBeDefined()
    expect(wrapper.emitted().click!.length).toBe(1)
  })
})
