import ContentLoading from '@/views/layout/content/ContentLoading.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('ContentLoading', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(ContentLoading)
    expect(wrapper.element).toMatchSnapshot()
  })
})
