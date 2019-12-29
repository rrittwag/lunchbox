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
      <RouterLinkIcon to="/" exact title="Mittagsangebote öffnen">
        <OffersIcon />
      </RouterLinkIcon>

      <RouterLinkIcon to="/settings" title="Einstellungen öffnen">
        <SettingsIcon />
      </RouterLinkIcon>

      <RouterLinkIcon to="/about" title="Info öffnen">
        <InfoIcon />
      </RouterLinkIcon>
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
import RouterLinkIcon from '@/views/layout/RouterLinkIcon.vue'

@Component({
  components: {
    RouterLinkIcon,
    DaySelector,
    LocationSelector,
    OffersIcon,
    SettingsIcon,
    InfoIcon,
  },
})
export default class Header extends Vue {}
</script>
