import { computed, ref } from 'vue'

export interface Theme {
  cssClass: string
  label: string
}

export type ColorScheme = 'system' | 'light' | 'dark'

export const THEME_RED: Theme = { cssClass: 'theme-red', label: 'Red' }
export const THEME_GREEN: Theme = { cssClass: 'theme-green', label: 'Green' }
export const THEME_BLUE: Theme = { cssClass: 'theme-blue', label: 'Blue' }
export const THEME_BLUE_DARK: Theme = { cssClass: 'theme-blue-dark', label: 'Blue (Dark)' }

const currentTheme = ref<Theme | undefined>()
const colorScheme = ref<ColorScheme>('system')

export function useTheme() {
  // TODO: register listener for media query
  if (colorScheme.value === 'system')
    colorScheme.value = window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'

  // TODO: Theme von ColorScheme lösen?
  setCurrentTheme(colorScheme.value === 'dark' ? THEME_BLUE_DARK : THEME_RED)

  // TODO: Umstellen auf Tailwind Dark Mode 'class'? -> https://tailwindcss.com/docs/dark-mode
  function setCurrentTheme(newTheme?: Theme) {
    // background color works best on <body />, so theme must be set on <body />
    const bodyClasses = window.document.body.classList
    if (currentTheme.value) bodyClasses.remove(currentTheme.value.cssClass)
    if (newTheme) bodyClasses.add(newTheme.cssClass)

    // transition on theme change AFTER initial page load
    if (currentTheme.value && !bodyClasses.contains('theme-transition'))
      bodyClasses.add('theme-transition')

    currentTheme.value = newTheme
  }

  function setColorScheme(newScheme: ColorScheme) {
    colorScheme.value = newScheme
  }

  return {
    themes: computed(() => [THEME_RED, THEME_GREEN, THEME_BLUE, THEME_BLUE_DARK]),
    currentTheme: computed(() => currentTheme.value),
    setCurrentTheme,
    colorScheme: computed(() => colorScheme.value),
    setColorScheme,
  }
}

export function __reset__TEST_ONLY__() {
  currentTheme.value = undefined
}
