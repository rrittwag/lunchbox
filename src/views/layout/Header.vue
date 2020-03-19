<template>
  <header
    class="flex flex-wrap justify-between items-center
           p-4"
  >
    <DaySelector class="w-full sm:max-w-sm" />

    <nav
      class="flex justify-center
             w-full sm:w-auto
             order-first sm:order-last
             px-4 pb-4 sm:pb-0"
      aria-label="Main"
    >
      <ul class="flex list-none p-0">
        <li class="inline-block mx-2">
          <NavLink to="/" exact title="Mittagsangebote">
            <OffersIcon />
          </NavLink>
        </li>

        <li class="inline-block mx-2">
          <NavLink to="/settings" title="Einstellungen">
            <SettingsIcon />
          </NavLink>
        </li>

        <li class="inline-block mx-2">
          <NavLink to="/about" title="Info">
            <InfoIcon />
          </NavLink>
        </li>
      </ul>
    </nav>
  </header>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import LocationSelector from './LocationSelector.vue'
import DaySelector from '@/views/offers/DaySelector.vue'
import OffersIcon from '@/assets/icons/offers.svg'
import SettingsIcon from '@/assets/icons/settings.svg'
import InfoIcon from '@/assets/icons/info.svg'
import NavLink from '@/views/layout/header/NavLink.vue'

@Component({
  components: {
    NavLink,
    DaySelector,
    LocationSelector,
    OffersIcon,
    SettingsIcon,
    InfoIcon,
  },
})
export default class Header extends Vue {}
</script>
