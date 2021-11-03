<template>
  <li class="w-full pt-3 sm:pt-4">
    <div class="flex items-baseline w-full">
      <h4 class="flex-grow text-xl text-neutral-800 leading-tight">
        {{ props.offer.name }}
      </h4>
      <span
        v-if="props.offer.price"
        class="pl-2 text-xl text-neutral-800 leading-tight whitespace-nowrap"
      >
        <small class="pl-2 text-lg font-light text-neutral-800"> € </small>
        {{ priceAsString }}
      </span>
    </div>

    <span
      class="sm:block pt-px pl-px font-light text-neutral-600 leading-snug"
      :class="props.showDetailsInXS ? 'flex' : 'hidden'"
    >
      {{ props.offer.description }}
    </span>

    <div class="sm:flex pt-1" :class="props.showDetailsInXS ? 'flex' : 'hidden'">
      <Badge
        v-for="tagLabel in sortedTags"
        :key="tagLabel"
        :label="tagLabel"
        :color="isVeggie(tagLabel) ? 'bg-success-200' : 'bg-accent-200'"
      />
    </div>
  </li>
</template>

<script setup lang="ts">
import { defineProps, computed } from 'vue'
import Badge from '@/views/offers/Badge.vue'
import { LunchOffer } from '@/model/LunchOffer'
import { formatEuro } from '@/util/formatting'

const props = defineProps<{
  offer: LunchOffer
  showDetailsInXS?: boolean
}>()

const sortedTags = computed(() => props.offer.tags.slice().sort())
const priceAsString = computed(() => formatEuro(props.offer.price))
const isVeggie = (tagLabel: string) => tagLabel === 'vegetarisch' || tagLabel === 'vegan'
</script>
