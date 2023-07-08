<template>
  <header class="pb-2 pt-4 sm:absolute sm:right-0 sm:top-0 sm:px-8 sm:pb-4">
    <Nav>
      <NavLink to="/">
        <OffersIcon aria-hidden="true" />
      </NavLink>

      <NavLink to="/settings">
        <SettingsIcon aria-hidden="true" />
      </NavLink>

      <NavLink to="/about">
        <InfoIcon aria-hidden="true" />
      </NavLink>
    </Nav>
  </header>
</template>

<script setup lang="ts">
import OffersIcon from '~icons/fa-solid/utensils'
import SettingsIcon from '~icons/fa-solid/cog'
import InfoIcon from '~icons/fa-solid/info-circle'
import NavLink from '@/views/layout/header/NavLink.vue'
import Nav from '@/views/layout/header/Nav.vue'
</script>
