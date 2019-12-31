import Settings from '@/views/Settings.vue'
import { createMock, mountUnit } from '@tests/unit/test-util'
import { ThemeStore } from '@/store'

describe('Settings', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(Settings, {}, { provide })
    expect(wrapper.element).toMatchSnapshot()
  })
})

// --- mocks 'n' stuff

const mockStore = createMock(ThemeStore)
const provide = { themeStore: mockStore }
