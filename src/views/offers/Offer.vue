<template>
  <div
    class="flex flex-col
           w-full
           pt-4"
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
               text-xl text-neutral-900 whitespace-no-wrap"
      >
        <span
          class="pl-2
                 text-lg font-light text-neutral-900"
          title="Euro"
          aria-label="Euro"
        >
          €
        </span>
        {{ priceAsString }}
      </span>
    </div>

    <span
      class="hidden sm:block
             pl-1
             font-light text-neutral-600"
    >
      {{ offer.description }}
    </span>

    <div
      class="hidden sm:flex
             pt-1"
    >
      <Badge
        v-for="tagLabel in sortedTags"
        :key="tagLabel"
        :label="tagLabel"
        :color="isVeggie(tagLabel) ? 'bg-success-200' : 'bg-accent-200'"
      />
    </div>
  </div>
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

  isVeggie(tagLabel: string): Boolean {
    return tagLabel === 'vegetarisch' || tagLabel === 'vegan'
  }
}
</script>
