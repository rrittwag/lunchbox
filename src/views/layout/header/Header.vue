<template>
  <b-navbar toggleable="sm" type="light">

    <b-navbar-brand to="/">
      <img alt="Vue logo" width="32" src="@/assets/lunchbox_logo.png">
      Lunchbox
    </b-navbar-brand>

    <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

    <b-collapse is-nav id="nav_collapse">
        <b-navbar-nav>
          <LocationSelector/>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
            <b-nav-item to="/" exact>Mittagsangebote</b-nav-item>
            <b-nav-item to="/settings" class="d-block d-sm-none">Einstellungen</b-nav-item>
            <b-nav-item to="/about">Info</b-nav-item>
        </b-navbar-nav>
    </b-collapse>

  </b-navbar>
</template>



<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import LocationSelector from './LocationSelector.vue'

@Component({
  components: {
    LocationSelector,
  },
})
export default class Header extends Vue {
}
</script>
