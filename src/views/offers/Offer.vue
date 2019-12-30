<template>
  <div
    class="flex flex-col
           w-full pt-4"
  >
    <div class="flex items-baseline w-full">
      <span
        class="flex-grow
               text-xl text-neutral-900"
      >
        {{ offer.name }}
      </span>
      <span
        class="pl-2
               text-lg font-light text-neutral-900"
      >
        €
      </span>
      <span
        class="pl-2
               text-xl text-neutral-900"
      >
        {{ offer.price | formatEuro }}
      </span>
    </div>

    <span
      class="hidden sm:block
             pl-1
             font-light text-neutral-900"
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
