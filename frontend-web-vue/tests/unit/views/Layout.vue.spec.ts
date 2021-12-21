import Layout from '@/views/Layout.vue'
import { shallowMount } from '@vue/test-utils'
import { createTestingPinia } from '@pinia/testing'

describe('Layout', () => {
  test('renders snapshot', () => {
    const pinia = createTestingPinia()

    const wrapper = shallowMount(Layout, {
      global: { plugins: [pinia] },
    })

    expect(wrapper.element).toMatchSnapshot()
  })
})
