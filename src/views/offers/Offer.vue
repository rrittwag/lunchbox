<template>
  <div
    class="flex flex-col
           w-full pt-4"
  >
    <div class="flex items-baseline w-full">
      <span class="flex-grow text-xl">{{ offer.name }}</span>
      <span class="text-lg font-light pl-2">€</span>
      <span class="text-xl pl-2">{{ offer.price | formatEuro }}</span>
    </div>
    <span
      class="hidden sm:block
               pl-1 text-gray-900 font-light"
    >
      {{ offer.description }}
    </span>
    <div class="hidden sm:flex pt-1">
      <Badge v-for="tagLabel in offer.tags" :key="tagLabel" :label="tagLabel" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { LunchOffer } from '@/model'
import Badge from '@/views/offers/Badge.vue'

@Component({
  components: {
    Badge,
  },
})
export default class Offer extends Vue {
  @Prop() offer!: LunchOffer
}
</script>
