<template>
  <header
    class="flex flex-wrap justify-between items-center
           p-4"
  >
    <div
      class="flex items-center justify-center sm:justify-start
             w-full sm:h-16 sm:max-w-sm"
    >
      <DaySelector v-if="$route.meta.showDaySelector" class="w-full" />
      <span v-else class="px-4 text-2xl text-neutral-800">
        {{ $route.meta.title }}
      </span>
    </div>

    <nav
      class="flex justify-center
             w-full sm:w-auto
             order-first sm:order-last
             px-4 pb-2 sm:pb-0"
      aria-label="Main"
    >
      <ul class="flex list-none p-0">
        <li class="inline-block mx-2">
          <NavLink to="/" exact>
            <OffersIcon />
          </NavLink>
        </li>

        <li class="inline-block mx-2">
          <NavLink to="/settings">
            <SettingsIcon />
          </NavLink>
        </li>

        <li class="inline-block mx-2">
          <NavLink to="/about">
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
