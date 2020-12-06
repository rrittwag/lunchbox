import Layout from '/@/views/Layout.vue'
import { createMock, mountUnit } from '/@tests/unit/test-util'
import { ThemeStore } from '/@/store/modules/ThemeStore'

describe('Layout', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(Layout, {}, { provide })
    expect(wrapper.element).toMatchSnapshot()
  })
})

// --- mocks 'n' stuff

const mockStore = createMock(ThemeStore)
const provide = { themeStore: mockStore }
