import { shallowMount } from '@vue/test-utils'
import Offers from '@/components/Offers.vue'

describe('Offers.vue', () => {
  it('renders props.providerName when passed', () => {
    const providerName = 'MyProvider'
    const wrapper = shallowMount(Offers, {
      propsData: { providerName },
    })
    expect(wrapper.text()).toMatch(providerName)
  })
})
