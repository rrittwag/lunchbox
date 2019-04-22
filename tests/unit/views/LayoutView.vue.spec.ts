import LayoutView from '@/views/LayoutView.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('LayoutView', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(LayoutView)
    expect(wrapper.element).toMatchSnapshot()
  })
})
