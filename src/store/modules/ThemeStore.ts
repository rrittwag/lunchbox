import { ActionResult, store } from '@/store'
import { Action, Module, Mutation, VuexModule } from 'vuex-module-decorators'

// --- mdoel ---

export interface Theme {
  value: string
  text: string
}

export const THEME_RED = { value: 'theme-red', text: 'Red' }
export const THEME_GREEN = { value: 'theme-green', text: 'Green' }
export const THEME_BLUE = { value: 'theme-blue', text: 'Blue' }
export const THEME_BLUE_DARK = { value: 'theme-blue-dark', text: 'Blue (Dark)' }

// --- mutations ---

const SET_THEME = 'SET_THEME'

// --- module ---

@Module({ store, dynamic: true, namespaced: true, name: 'theme' })
export class ThemeStore extends VuexModule {
  // --- state ---

  theme: Theme = this.initialTheme()

  readonly themes: Theme[] = [THEME_RED, THEME_GREEN, THEME_BLUE, THEME_BLUE_DARK]

  @Mutation
  protected [SET_THEME](theme: Theme | string) {
    if (typeof theme === 'string')
      this.themes
        .filter(t => t.value === theme)
        .forEach(t => {
          this.theme = t
        })
    else this.theme = theme
  }

  @Action
  async updateTheme(theme: Theme | string): Promise<ActionResult> {
    try {
      this.context.commit(SET_THEME, theme)
      return ActionResult.success('Theme gespeichert')
    } catch (error) {
      return ActionResult.error('Theme speichern', error.message)
    }
  }

  protected initialTheme(): Theme {
    // if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches)
    //   return THEME_BLUE_DARK
    return THEME_BLUE
  }
}
