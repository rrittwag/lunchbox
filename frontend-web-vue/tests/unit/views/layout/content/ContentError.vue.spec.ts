import ContentError from '@/views/layout/content/ContentError.vue'
import { mount } from '@vue/test-utils'

describe('ContentError', () => {
  test('renders snapshot', () => {
    const wrapper = mount(ContentError)
    expect(wrapper.element).toMatchSnapshot()
  })
})
