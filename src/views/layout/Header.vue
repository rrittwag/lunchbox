<template>
  <header
    class="flex flex-wrap justify-between items-center
           p-4"
  >
    <nav
      class="flex justify-center
             w-full sm:w-auto
             px-4 pb-2 sm:pb-0"
      aria-label="Haupt"
    >
      <ul class="flex list-none p-0">
        <NavLink to="/" exact>
          <OffersIcon />
        </NavLink>

        <NavLink to="/settings">
          <SettingsIcon />
        </NavLink>

        <NavLink to="/about">
          <InfoIcon />
        </NavLink>
      </ul>
    </nav>

    <transition name="fade" mode="out-in">
      <div
        class="flex items-center justify-center sm:justify-start
               sm:order-first
               w-full sm:h-16 sm:max-w-sm"
        :key="$route.path"
      >
        <h1
          class="px-4 text-2xl text-neutral-800"
          :class="{ 'sr-only': $route.meta.showDaySelector }"
        >
          {{ $route.meta.title }}
        </h1>
        <DaySelector v-if="$route.meta.showDaySelector" class="w-full" />
      </div>
    </transition>
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
