<template>
  <div
    class="flex flex-wrap justify-between items-center
           px-4 py-4"
  >
    <DaySelector class="w-full sm:max-w-sm" />

    <nav
      class="flex justify-center
             w-full sm:w-auto
             order-first sm:order-last
             px-4 pb-4 sm:pb-0"
      aria-label="Main"
    >
      <router-link to="/" exact v-slot="{ navigate, isActive }">
        <button
          class="flex justify-center items-center
                 w-12 h-12
                 mx-2"
          :class="isActive ? 'text-gray-500' : 'text-gray-900'"
          @click="navigate"
          title="Mittagsangebote öffnen"
          aria-label="Mittagsangebote öffnen"
        >
          <OffersIcon class="w-8 h-8" />
        </button>
      </router-link>
      <router-link to="/settings" v-slot="{ navigate, isActive }">
        <button
          class="flex justify-center items-center
                 w-12 h-12
                 mx-2"
          :class="isActive ? 'text-gray-500' : 'text-gray-900'"
          @click="navigate"
          title="Einstellungen öffnen"
          aria-label="Einstellungen öffnen"
        >
          <SettingsIcon class="w-8 h-8" />
        </button>
      </router-link>
      <router-link to="/about" v-slot="{ navigate, isActive }">
        <button
          class="flex justify-center items-center
                 w-12 h-12
                 mx-2"
          :class="isActive ? 'text-gray-500' : 'text-gray-900'"
          @click="navigate"
          title="Info öffnen"
          aria-label="Info öffnen"
        >
          <InfoIcon class="w-8 h-8" />
        </button>
      </router-link>
    </nav>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import LocationSelector from './LocationSelector.vue'
import DaySelector from '@/views/offers/DaySelector.vue'
import OffersIcon from '@/assets/icons/offers.svg'
import SettingsIcon from '@/assets/icons/settings.svg'
import InfoIcon from '@/assets/icons/info.svg'

@Component({
  components: {
    DaySelector,
    LocationSelector,
    OffersIcon,
    SettingsIcon,
    InfoIcon,
  },
})
export default class Header extends Vue {}
</script>
