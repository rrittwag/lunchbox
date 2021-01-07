import Settings from '@/views/Settings.vue'
import { mocked } from 'ts-jest/utils'
jest.mock('@/store/theme')
import { useTheme } from '@/store/theme'
import { shallowMount } from '@vue/test-utils'
import { computed } from 'vue'
import { themeBlue, themeGreen, themeRed } from '@tests/unit/test-data'

describe('Settings', () => {
  test('renders snapshot', () => {
    mocked(useTheme).mockReturnValue({
      themes: computed(() => [themeRed, themeGreen, themeBlue]),
      currentTheme: computed(() => themeRed),
      setCurrentTheme: () => void 0,
      colorScheme: computed(() => 'system'),
      setColorScheme: () => void 0,
    })

    const wrapper = shallowMount(Settings)

    expect(wrapper.element).toMatchSnapshot()
  })
})
