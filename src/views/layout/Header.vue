<template>
  <header
    class="flex flex-wrap justify-between items-center
           sm:absolute sm:top-0 sm:right-0
           px-8 pt-4 pb-2 sm:pb-4"
  >
    <nav
      class="flex justify-center
             w-full sm:w-auto"
      aria-label="Haupt"
    >
      <ul
        class="flex items-center
               sm:h-16
               list-none"
      >
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
  </header>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import OffersIcon from '@/assets/icons/offers.svg'
import SettingsIcon from '@/assets/icons/settings.svg'
import InfoIcon from '@/assets/icons/info.svg'
import NavLink from '@/views/layout/header/NavLink.vue'

@Component({
  components: {
    NavLink,
    OffersIcon,
    SettingsIcon,
    InfoIcon,
  },
})
export default class Header extends Vue {}
</script>
