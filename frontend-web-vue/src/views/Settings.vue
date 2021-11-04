<template>
  <div class="sm:p-4">
    <h1
      class="
        flex
        items-center
        justify-center
        sm:justify-start sm:h-16
        px-4
        text-2xl text-neutral-800
      "
    >
      Einstellungen
    </h1>
    <div>
      <label for="settings-theme"> Theme: </label>
      <div class="m-8 inline-block relative w-32">
        <select
          id="settings-theme"
          class="
            block
            appearance-none
            w-full
            bg-neutral-200
            text-neutral-900
            px-4
            py-2
            pr-8
            rounded
            leading-tight
            border border-neutral-400
            hover:border-neutral-800
            focus:outline-none focus:ring-4
          "
          name="theme"
          :value="currentTheme?.cssClass"
          @change="onSelectTheme"
        >
          <option v-for="theme in themes" :key="theme.cssClass" :value="theme.cssClass">
            {{ theme.label }}
          </option>
        </select>
        <div
          class="
            pointer-events-none
            absolute
            inset-y-0
            right-0
            flex
            items-center
            px-2
            text-neutral-900
          "
        >
          <svg class="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
            <path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" />
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useTheme } from '@/store/theme'
import { storeToRefs } from 'pinia'

const store = useTheme()
const { themes, currentTheme } = storeToRefs(store)

function onSelectTheme(event: Event) {
  const selectElem = event?.target as HTMLSelectElement
  const newTheme = themes.value.find((theme) => theme.cssClass === selectElem?.value)
  newTheme && store.setCurrentTheme(newTheme)
}
</script>
