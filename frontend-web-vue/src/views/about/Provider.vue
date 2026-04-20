<script setup lang="ts">
import lunchLink from '@/views/about/components/LunchLink.vue'
</script>

<template>
  <div id="infobox-providers">
    <h2 class="text-3xl">
      Mittagsanbieter
    </h2>
    <table>
      <tbody class="align-top">
        <tr class="border-b">
          <td>
            Neubrandenburg
          </td>
          <td>
            <lunchLink text="Suppenkulttour" href="http://www.suppenkult.com/wochenplan.html" />
            <lunchLink text="Boulevard Imbiss" href="https://www.boulevard-imbiss.de/" />
            <lunchLink text="BOS Bistro Café" href="https://www.torney-landfleischerei.de/" />
          </td>
        </tr>
        <tr>
          <td>
            Berlin Springpfuhl
          </td>
          <td>
            <lunchLink text="Salt 'n' Pepper" href="https://www.partyservice-rohde.de/bistro-angebot-der-woche/" />
            <lunchLink text="Cafeteria Uwe Gläser" href="https://cafeteria-uwe-glaser.business.site/" />
            <lunchLink text="Feldküche Karow" href="http://www.feldkuechebkarow.de/speiseplan/" />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
