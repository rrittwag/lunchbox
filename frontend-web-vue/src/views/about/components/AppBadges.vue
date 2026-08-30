<script setup lang="ts">
import Dropdown from '@/views/about/components/Dropdown.vue'
import Tooltip from '@/views/about/components/Tooltip.vue'
</script>

<template>
  <div id="app-badges" class="sm:flex flex-initial w-xl text-center sm:gap-12 gap-4 mt-4 grid grid-cols-1">
    <div class="flex gap-12">
      <Tooltip text="Web-App">
        <a class="btn" href="/">
          <img alt="Web-App" width="60px" height="60px" src="@/assets/images/webapp_icon.png">
        </a>
      </Tooltip>
      <Dropdown>
        <template #drMain>
          <Tooltip text="Web-Feed" alignment="top">
            <a class="btn dropdown-toggle" uib-dropdown-toggle tooltip-is-open="about.tooltipIsOpen">
              <img alt="Web-Feed" width="60px" height="60px" src="@/assets/images/webfeed_icon.png">
            </a>
          </Tooltip>
        </template>
        <template #drContent>
          <ul class="dropdown-menu" role="menu">
            <!-- TODO: Feed-Links dynamisch anhand vorhandener LunchProvider generieren -->
            <li role="presentation">
              <a
                role="menuitem" tabindex="-1" href="http://lunchbox.rori.info/feed?location=Neubrandenburg"
                target="_blank"
              >Atom Web-Feed Neubrandenburg</a>
            </li>
            <li role="presentation">
              <a
                role="menuitem" tabindex="-1" href="http://lunchbox.rori.info/feed?location=Berlin Springpfuhl"
                target="_blank"
              >Atom Web-Feed Berlin Springpfuhl</a>
            </li>
          </ul>
        </template>
      </Dropdown>
    </div>
    <Tooltip text="Android-App">
      <div>
        <a
          class="btn" href="http://play.google.com/store/apps/details?id=info.rori.lunchbox.client_android"
          target="_blank"
        >
          <img alt="Android app on Google Play" src="@/assets/images/playstore_icon.png">
        </a>
      </div>
    </Tooltip>
    <Tooltip text="iOS-App (in Arbeit)">
      <div>
        <a class="btn disabled" href="http://appstore.com/companyname/appname" target="_blank">
          <img alt="Download on the App Store" src="@/assets/images/appstore_icon.png">
        </a>
      </div>
    </Tooltip>
  </div>
</template>

<style>
.disabled {
  opacity: 0.3;
  pointer-events: none;
}
</style>
