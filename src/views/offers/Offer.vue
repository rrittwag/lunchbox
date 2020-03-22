<template>
  <li
    class="flex flex-col
           w-full
           pt-3 sm:pt-4"
  >
    <div
      class="order-last
             pt-1
             hidden sm:flex"
    >
      <Badge
        v-for="tagLabel in sortedTags"
        :key="tagLabel"
        :label="tagLabel"
        :color="isVeggie(tagLabel) ? 'bg-success-200' : 'bg-accent-200'"
      />
    </div>

    <div class="flex items-baseline w-full">
      <span
        class="flex-grow
               text-xl text-neutral-900 leading-tight"
      >
        {{ offer.name }}
      </span>
      <span
        class="pl-2
               text-xl text-neutral-900 leading-tight whitespace-no-wrap"
      >
        <small
          class="pl-2
                 text-lg font-light text-neutral-900"
        >
          €
        </small>
        {{ priceAsString }}
      </span>
    </div>

    <span
      class="hidden sm:block
             pt-px pl-px
             font-light text-neutral-600 leading-snug"
    >
      {{ offer.description }}
    </span>
  </li>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import Badge from '@/views/offers/Badge.vue'
import { LunchOffer } from '@/model/LunchOffer'
import { formatEuro } from '@/util/formatting'

@Component({
  components: {
    Badge,
  },
})
export default class Offer extends Vue {
  @Prop() offer!: LunchOffer

  get sortedTags() {
    return this.offer.tags.sort()
  }

  get priceAsString(): string {
    return formatEuro(this.offer.price)
  }

  isVeggie(tagLabel: string): boolean {
    return tagLabel === 'vegetarisch' || tagLabel === 'vegan'
  }
}
</script>
