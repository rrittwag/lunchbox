<script setup lang="ts">
import { useTheme } from '@/store/theme'
import { storeToRefs } from 'pinia'

const store = useTheme()
const { themes, currentTheme } = storeToRefs(store)

function onSelectTheme(event: Event) {
  const selectElem = event.target as HTMLSelectElement
  const newTheme = themes.value.find(theme => theme.cssClass === selectElem?.value)
  if (newTheme)
    store.setCurrentTheme(newTheme)
}
</script>

<template>
  <div class="sm:p-4">
    <h1 class="flex items-center justify-center px-4 text-2xl text-neutral-800 sm:h-16 sm:justify-start">
      Einstellungen
    </h1>
    <div>
      <div class="mx-auto max-w-screen-xl px-4 py-12 sm:px-6 lg:px-8 lg:py-16">
        <div class="mt-6 border-t-2 border-neutral-500 pt-6">
          <dl>
            <div class="md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base font-medium leading-6 text-neutral-900 md:col-span-5">
                Ort
              </dt>
              <dd class="mt-2 md:col-span-7 md:mt-0">
                <button class="h-20 rounded-sm border border-primary-500 px-4 text-base leading-6 text-neutral-800">
                  Neubrandenburg
                </button>
                <button
                  class="ml-2 h-20 rounded-sm border border-neutral-400 px-4 text-base leading-6 text-neutral-500 hover:border-neutral-800"
                >
                  Neubrandenburg
                </button>
                <button
                  class="ml-2 h-20 rounded-sm border border-neutral-400 px-4 text-base leading-6 text-neutral-500 hover:border-neutral-800"
                >
                  Berlin Springpfuhl
                </button>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base font-medium leading-6 text-neutral-900 md:col-span-5" for="settings-theme">
                Farbvariante
              </dt>
              <dd class="mt-2 md:col-span-7 md:mt-0">
                <div class="relative inline-block w-32">
                  <select
                    id="settings-theme"
                    class="block w-full appearance-none rounded-sm border border-neutral-400 bg-neutral-200 px-4 py-2 pr-8 leading-tight text-neutral-900 hover:border-neutral-800 focus:outline-hidden focus:ring-4"
                    name="theme"
                    :value="currentTheme?.cssClass"
                    @change="onSelectTheme"
                  >
                    <option v-for="theme in themes" :key="theme.cssClass" :value="theme.cssClass">
                      {{ theme.label }}
                    </option>
                  </select>
                  <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-neutral-900">
                    <svg class="h-4 w-4 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                      <path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" />
                    </svg>
                  </div>
                </div>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base font-medium leading-6 text-neutral-900 md:col-span-5">
                Dark Mode
              </dt>
              <dd class="mt-2 md:col-span-7 md:mt-0">
                <p class="text-base leading-6 text-neutral-500">
                  ja/nein/System
                </p>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base font-medium leading-6 text-neutral-900 md:col-span-5">
                Mittagsanbieter anzeigen
              </dt>
              <dd class="mt-2 md:col-span-7 md:mt-0">
                <p class="text-base leading-6 text-neutral-500">
                  Because they're so good at it. Lorem ipsum dolor sit amet consectetur adipisicing elit. Quas
                  cupiditate laboriosam fugiat.
                </p>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base font-medium leading-6 text-neutral-900 md:col-span-5">
                Mittagsdetails immer einblenden
              </dt>
              <dd class="mt-2 md:col-span-7 md:mt-0">
                <p class="text-base leading-6 text-neutral-500">
                  He couldn't see himself doing it. Lorem ipsum dolor sit, amet consectetur adipisicing elit. Eveniet
                  perspiciatis officiis corrupti tenetur. Temporibus ut voluptatibus, perferendis sed unde rerum
                  deserunt eius.
                </p>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base font-medium leading-6 text-neutral-900 md:col-span-5">
                Animationen ausschalten
              </dt>
              <dd class="mt-2 md:col-span-7 md:mt-0">
                <p class="text-base leading-6 text-neutral-500">
                  He couldn't see himself doing it. Lorem ipsum dolor sit, amet consectetur adipisicing elit. Eveniet
                  perspiciatis officiis corrupti tenetur. Temporibus ut voluptatibus, perferendis sed unde rerum
                  deserunt eius.
                </p>
              </dd>
            </div>
          </dl>
        </div>
      </div>
    </div>
  </div>
</template>
