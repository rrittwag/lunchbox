import Settings from '@/views/Settings.vue'
import { useTheme, THEME_RED } from '@/store/theme'
import { shallowMount } from '@vue/test-utils'
import { createTestingPinia } from '@pinia/testing'

describe('Settings', () => {
  test('renders snapshot', () => {
    const pinia = createTestingPinia()
    useTheme().$patch({ currentTheme: THEME_RED, colorScheme: 'system' })

    const wrapper = shallowMount(Settings, {
      global: { plugins: [pinia] },
    })

    expect(wrapper.element).toMatchSnapshot()
  })
})
