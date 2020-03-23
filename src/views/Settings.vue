<template>
  <div class="sm:p-4">
    <h1
      class="flex items-center justify-center sm:justify-start
             sm:h-16 px-4
             text-2xl text-neutral-800"
    >
      Einstellungen
    </h1>
    <div>
      <label id="settings-theme">
        Theme:
      </label>
      <div class="m-8 inline-block relative w-32">
        <select
          class="block appearance-none w-full
                 bg-neutral-200 text-neutral-900
                 px-4 py-2 pr-8 rounded leading-tight
                 border border-neutral-400 hover:border-neutral-800
                 focus:outline-none focus:shadow-outline"
          name="theme"
          aria-labelledby="settings-theme"
          :value="themeStore.theme.value"
          @change="updateTheme"
        >
          <option v-for="theme in themeStore.themes" :key="theme.value" :value="theme.value">
            {{ theme.text }}
          </option>
        </select>
        <div
          class="pointer-events-none absolute inset-y-0 right-0
                 flex items-center
                 px-2
                 text-neutral-900"
        >
          <svg class="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
            <path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" />
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Provide } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import { ThemeStore } from '@/store/modules/ThemeStore'

@Component
export default class Settings extends Vue {
  @Provide() themeStore: ThemeStore = getModule(ThemeStore)

  updateTheme(event: { target: HTMLSelectElement }) {
    this.themeStore.updateTheme(event.target.value)
  }
}
</script>
