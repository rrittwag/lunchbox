<script setup lang="ts">
import type { LunchOffer } from '@/model/lunch'
import { computed } from 'vue'
import { formatEuro } from '@/util/formatting'
import Badge from '@/views/offers/Badge.vue'

const props = defineProps<{
  offer: LunchOffer
  showDetailsInXS?: boolean
}>()

const sortedTags = computed(() => props.offer.tags.slice().sort())
const priceAsString = computed(() => formatEuro(props.offer.price))
const isVeggie = (tagLabel: string) => tagLabel === 'vegetarisch' || tagLabel === 'vegan'
</script>

<template>
  <li class="w-full pt-3 sm:pt-4">
    <div class="flex w-full items-baseline">
      <h4 class="grow text-xl leading-tight text-neutral-800">
        {{ props.offer.name }}
      </h4>
      <span v-if="props.offer.price" aria-label="Preis" class="shrink-0 pl-2 text-xl leading-tight text-neutral-800">
        <small class="pl-2 pr-1 text-lg font-light text-neutral-800">€</small>{{ priceAsString }}
      </span>
    </div>

    <p
      role="note"
      class="pl-px pt-px font-light leading-snug text-neutral-600 sm:block"
      :class="props.showDetailsInXS ? 'flex' : 'hidden'"
    >
      {{ props.offer.description }}
    </p>

    <p class="pt-1 sm:flex" :class="props.showDetailsInXS ? 'flex' : 'hidden'">
      <Badge
        v-for="tagLabel in sortedTags"
        :key="tagLabel"
        role="note"
        :label="tagLabel"
        :color="isVeggie(tagLabel) ? 'bg-success-200' : 'bg-accent-200'"
      />
    </p>
  </li>
</template>
