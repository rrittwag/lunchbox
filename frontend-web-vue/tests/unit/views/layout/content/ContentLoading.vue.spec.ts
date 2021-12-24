import ContentLoading from '@/views/layout/content/ContentLoading.vue'
import { mount } from '@vue/test-utils'
import { describe, test, expect } from 'vitest'

describe('ContentLoading', () => {
  test('renders snapshot', () => {
    const wrapper = mount(ContentLoading)
    expect(wrapper.element).toMatchSnapshot()
  })
})
