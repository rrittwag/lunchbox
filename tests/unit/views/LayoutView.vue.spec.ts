import LayoutView from '@/views/LayoutView.vue'
import { createMock, mountUnit } from '@tests/unit/test-util'
import { ThemeStore } from '@/store/modules/ThemeStore'

describe('LayoutView', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(LayoutView, {}, { provide })
    expect(wrapper.element).toMatchSnapshot()
  })
})

// --- mocks 'n' stuff

const mockStore = createMock(ThemeStore)
const provide = { themeStore: mockStore }
