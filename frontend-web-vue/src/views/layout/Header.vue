<template>
  <header class="sm:absolute sm:top-0 sm:right-0 sm:px-8 pt-4 pb-2 sm:pb-4">
    <Nav>
      <NavLink to="/">
        <OffersIcon />
      </NavLink>

      <NavLink to="/settings">
        <SettingsIcon />
      </NavLink>

      <NavLink to="/about">
        <InfoIcon />
      </NavLink>
    </Nav>
  </header>
</template>

<script setup lang="ts">
import OffersIcon from '@/assets/icons/offers.svg'
import SettingsIcon from '@/assets/icons/settings.svg'
import InfoIcon from '@/assets/icons/info.svg'
import NavLink from '@/views/layout/header/NavLink.vue'
import Nav from '@/views/layout/header/Nav.vue'
</script>
