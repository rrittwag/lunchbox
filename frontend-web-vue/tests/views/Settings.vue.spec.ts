import { THEME_RED, useTheme } from '@/store/theme'
import Settings from '@/views/Settings.vue'
import { render } from '@testing-library/vue'
import { createTestingPinia } from '@tests/test-utils'

it('renders', () => {
  const pinia = createTestingPinia()
  useTheme().$patch({ currentTheme: THEME_RED, colorScheme: 'system' })

  const { getByRole } = render(Settings, {
    global: { plugins: [pinia] },
  })

  expect(getByRole('heading', { level: 1 })).toHaveTextContent('Einstellungen')
  // TODO: test me!
})
