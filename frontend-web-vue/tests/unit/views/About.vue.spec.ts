import About from '@/views/About.vue'
import { shallowMount } from '@vue/test-utils'
import { describe, test, expect } from 'vitest'

describe('About', () => {
  test('renders snapshot', () => {
    const wrapper = shallowMount(About)
    expect(wrapper.element).toMatchSnapshot()
  })
})
