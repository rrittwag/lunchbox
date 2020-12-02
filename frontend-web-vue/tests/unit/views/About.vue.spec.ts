import About from '@/views/About.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('About', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(About)
    expect(wrapper.element).toMatchSnapshot()
  })
})
