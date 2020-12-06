import ContentError from '/@/views/layout/content/ContentError.vue'
import { mountUnit } from '/@tests/unit/test-util'

describe('ContentError', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(ContentError)
    expect(wrapper.element).toMatchSnapshot()
  })
})
