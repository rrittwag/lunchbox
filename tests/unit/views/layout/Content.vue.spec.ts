import Content from '@/views/layout/Content.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('Content', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(Content)
    expect(wrapper.element).toMatchSnapshot()
  })
})
