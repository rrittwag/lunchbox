<script setup lang="ts">
import dropdown from '@/views/about/components/Dropdown.vue'
import tooltip from '@/views/about/components/Tooltip.vue'
</script>

<template>
  <div id="app-badges" class="flex flex-initial w-xl text-center gap-12 mt-4">
    <div class="flex gap-12">
      <tooltip text="Web-App">
        <a class="btn" href="/">
          <img alt="Web-App" width="60px" height="60px" src="@/assets/images/webapp_icon.png">
        </a>
      </tooltip>
      <dropdown>
        <template #drMain>
          <tooltip text="Web-Feed" alignment="top">
            <a class="btn dropdown-toggle" uib-dropdown-toggle tooltip-is-open="about.tooltipIsOpen">
              <img alt="Web-Feed" width="60px" height="60px" src="@/assets/images/webfeed_icon.png">
            </a>
          </tooltip>
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
      </dropdown>
    </div>
    <tooltip text="Android-App">
      <div>
        <a
          class="btn" href="http://play.google.com/store/apps/details?id=info.rori.lunchbox.client_android"
          target="_blank"
        >
          <img alt="Android app on Google Play" src="@/assets/images/playstore_icon.png">
        </a>
      </div>
    </tooltip>
    <tooltip text="iOS-App (in Arbeit)">
      <div>
        <a class="btn disabled" href="http://appstore.com/companyname/appname" target="_blank">
          <img alt="Download on the App Store" src="@/assets/images/appstore_icon.png">
        </a>
      </div>
    </tooltip>
  </div>
</template>

<style>
.disabled {
  opacity: 0.3;
  pointer-events: none;
}
</style>
