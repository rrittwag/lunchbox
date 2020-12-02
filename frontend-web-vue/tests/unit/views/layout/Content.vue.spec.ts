import Content from '@/views/layout/Content.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('Content', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(Content, {}, { applyRouter: true })
    expect(wrapper.element).toMatchSnapshot()
  })
})
