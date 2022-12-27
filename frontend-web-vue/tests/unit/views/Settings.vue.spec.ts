import Settings from '@/views/Settings.vue'
import { useTheme, THEME_RED } from '@/store/theme'
import { createTestingPinia } from '@tests/unit/test-utils'
import { render } from '@testing-library/vue'

describe('Settings', () => {
  it('renders', () => {
    const pinia = createTestingPinia()
    useTheme().$patch({ currentTheme: THEME_RED, colorScheme: 'system' })

    const { getByRole } = render(Settings, {
      global: { plugins: [pinia] },
    })

    expect(getByRole('heading', { level: 1 })).toHaveTextContent('Einstellungen')
    // TODO: test me!
  })
})
