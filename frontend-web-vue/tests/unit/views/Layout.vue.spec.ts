import Layout from '@/views/Layout.vue'
import { shallowMount } from '@vue/test-utils'
import { describe, test, expect } from 'vitest'
import { createTestingPinia } from '@tests/unit/test-utils'

describe('Layout', () => {
  test('renders snapshot', () => {
    const pinia = createTestingPinia()

    const wrapper = shallowMount(Layout, {
      global: { plugins: [pinia] },
    })

    expect(wrapper.element).toMatchSnapshot()
  })
})
