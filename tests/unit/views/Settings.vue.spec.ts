import Settings from '@/views/Settings.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('Settings', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(Settings)
    expect(wrapper.element).toMatchSnapshot()
  })
})
