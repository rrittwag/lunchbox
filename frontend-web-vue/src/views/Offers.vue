<script setup lang="ts">
import type { DaySelectorDirection } from '@/views/offers/dayselector/DaySelector.values'
import { storeToRefs } from 'pinia'
import { computed, ref } from 'vue'
import { useLunchStore } from '@/store/lunch'
import { formatToISODate } from '@/util/formatting'
import ContentError from '@/views/layout/content/ContentError.vue'
import ContentLoading from '@/views/layout/content/ContentLoading.vue'
import DaySelector from '@/views/offers/DaySelector.vue'
import OfferBoxGroup from '@/views/offers/OfferBoxGroup.vue'

const store = useLunchStore()
const { isLoading, error, providers, offers, selectedLocation, selectedDay } = storeToRefs(store)
const isDirectionNext = ref(true)

const hasOffers = computed(() => offers.value.length > 0 && providers.value.length > 0)
const loadingDone = computed(() => !isLoading.value && !error.value)
const loadingFailed = computed(() => error.value)

const lunchDays = computed<Date[]>(() => {
  const providerIDsForSelectedLocation = providers.value
    .filter(p => p.location === selectedLocation.value.name)
    .map(p => p.id)
  const lunchDays: string[] = offers.value
    .filter(o => providerIDsForSelectedLocation.includes(o.provider))
    .map(o => o.day)
  return Array.from(new Set<string>(lunchDays))
    .map(dayString => new Date(dayString))
    .sort((day1, day2) => day1.getTime() - day2.getTime())
})
const prevDay = computed(() => lunchDays.value.filter(day => day < selectedDay.value).pop())
const nextDay = computed(() => lunchDays.value.filter(day => day > selectedDay.value)[0])

const selectedDayAsISOString = computed(() => formatToISODate(selectedDay.value))

function onDaySelected(direction: DaySelectorDirection) {
  const gotoDay = direction === 'next' ? nextDay.value : prevDay.value
  if (!gotoDay)
    return

  isDirectionNext.value = direction === 'next'
  store.selectDay(gotoDay)
}
/*
function onSwipe(swipeDirection: string) {
  if (swipeDirection === 'left') onDaySelected(DaySelectorDirection.NEXT)
  else if (swipeDirection === 'right') onDaySelected(DaySelectorDirection.PREVIOUS)
}
 */
</script>

<template>
  <div v-if="hasOffers || loadingDone" class="flex h-full flex-col sm:py-4">
    <div class="px-4">
      <h1 class="sr-only">
        Mittagsangebote
      </h1>
      <DaySelector
        :selected-day="selectedDay"
        :disabled-next="!nextDay"
        :disabled-prev="!prevDay"
        class="h-16 sm:max-w-sm"
        @change="onDaySelected"
      />
    </div>
    <Transition
      mode="out-in"
      leave-active-class="transition-all duration-100 ease-in"
      :leave-to-class="`opacity-0 ${isDirectionNext ? '-translate-x-12' : 'translate-x-12'}`"
      enter-active-class="delay-200 transition-all duration-50 ease-out"
      :enter-from-class="`opacity-0 ${isDirectionNext ? 'translate-x-1' : '-translate-x-1'}`"
    >
      <!--       <OfferBoxGroup
        v-touch:swipe="onSwipe"
        :key="selectedDayAsISOString"
        class="flex-grow
               pt-4"
      />
-->
      <OfferBoxGroup :key="selectedDayAsISOString" class="grow pt-4" />
    </Transition>
  </div>
  <ContentError v-else-if="loadingFailed" />
  <ContentLoading v-else />
</template>
