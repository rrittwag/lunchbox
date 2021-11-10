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
      <div class="max-w-screen-xl mx-auto py-12 px-4 sm:px-6 lg:py-16 lg:px-8">
        <div class="mt-6 border-t-2 border-neutral-500 pt-6">
          <dl>
            <div class="md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base leading-6 font-medium text-neutral-900 md:col-span-5">Ort</dt>
              <dd class="mt-2 md:mt-0 md:col-span-7">
                <button
                  class="
                    rounded
                    px-4
                    h-20
                    border border-primary-500
                    text-base
                    leading-6
                    text-neutral-800
                  "
                >
                  Neubrandenburg
                </button>
                <button
                  class="
                    rounded
                    ml-2
                    px-4
                    h-20
                    border border-neutral-400
                    hover:border-neutral-800
                    text-base
                    leading-6
                    text-neutral-500
                  "
                >
                  Neubrandenburg
                </button>
                <button
                  class="
                    rounded
                    ml-2
                    px-4
                    h-20
                    border border-neutral-400
                    hover:border-neutral-800
                    text-base
                    leading-6
                    text-neutral-500
                  "
                >
                  Berlin Springpfuhl
                </button>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt
                class="text-base leading-6 font-medium text-neutral-900 md:col-span-5"
                for="settings-theme"
              >
                Farbvariante
              </dt>
              <dd class="mt-2 md:mt-0 md:col-span-7">
                <div class="inline-block relative w-32">
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
                    <svg
                      class="fill-current h-4 w-4"
                      xmlns="http://www.w3.org/2000/svg"
                      viewBox="0 0 20 20"
                    >
                      <path
                        d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"
                      />
                    </svg>
                  </div>
                </div>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base leading-6 font-medium text-neutral-900 md:col-span-5">
                Dark Mode
              </dt>
              <dd class="mt-2 md:mt-0 md:col-span-7">
                <p class="text-base leading-6 text-neutral-500">ja/nein/System</p>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base leading-6 font-medium text-neutral-900 md:col-span-5">
                Mittagsanbieter anzeigen
              </dt>
              <dd class="mt-2 md:mt-0 md:col-span-7">
                <p class="text-base leading-6 text-neutral-500">
                  Because they're so good at it. Lorem ipsum dolor sit amet consectetur adipisicing
                  elit. Quas cupiditate laboriosam fugiat.
                </p>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base leading-6 font-medium text-neutral-900 md:col-span-5">
                Mittagsdetails immer einblenden
              </dt>
              <dd class="mt-2 md:mt-0 md:col-span-7">
                <p class="text-base leading-6 text-neutral-500">
                  He couldn't see himself doing it. Lorem ipsum dolor sit, amet consectetur
                  adipisicing elit. Eveniet perspiciatis officiis corrupti tenetur. Temporibus ut
                  voluptatibus, perferendis sed unde rerum deserunt eius.
                </p>
              </dd>
            </div>
            <div class="mt-8 border-t border-neutral-500 pt-6 md:grid md:grid-cols-12 md:gap-8">
              <dt class="text-base leading-6 font-medium text-neutral-900 md:col-span-5">
                Animationen ausschalten
              </dt>
              <dd class="mt-2 md:mt-0 md:col-span-7">
                <p class="text-base leading-6 text-neutral-500">
                  He couldn't see himself doing it. Lorem ipsum dolor sit, amet consectetur
                  adipisicing elit. Eveniet perspiciatis officiis corrupti tenetur. Temporibus ut
                  voluptatibus, perferendis sed unde rerum deserunt eius.
                </p>
              </dd>
            </div>
          </dl>
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
  const selectElem = event.target as HTMLSelectElement
  const newTheme = themes.value.find((theme) => theme.cssClass === selectElem?.value)
  if (newTheme) store.setCurrentTheme(newTheme)
}
</script>
