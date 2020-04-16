<template>
  <li
    class="w-full
           pt-3 sm:pt-4"
  >
    <div class="flex items-baseline w-full">
      <h4
        class="flex-grow
               text-xl text-neutral-800 leading-tight"
      >
        {{ offer.name }}
      </h4>
      <span
        class="pl-2
               text-xl text-neutral-800 leading-tight whitespace-no-wrap"
      >
        <small
          class="pl-2
                 text-lg font-light text-neutral-800"
        >
          €
        </small>
        {{ priceAsString }}
      </span>
    </div>

    <span
      class="sm:block
             pt-px pl-px
             font-light text-neutral-600 leading-snug"
      :class="showDetailsInXS ? 'flex' : 'hidden'"
    >
      {{ offer.description }}
    </span>

    <div
      class="sm:flex
             pt-1"
      :class="showDetailsInXS ? 'flex' : 'hidden'"
    >
      <Badge
        v-for="tagLabel in sortedTags"
        :key="tagLabel"
        :label="tagLabel"
        :color="isVeggie(tagLabel) ? 'bg-success-200' : 'bg-accent-200'"
      />
    </div>
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
  @Prop() showDetailsInXS!: boolean

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
