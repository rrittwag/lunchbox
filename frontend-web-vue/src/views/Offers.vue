<template>
  <div
    class="flex flex-col
           h-full
           sm:py-4"
    v-if="loadingDone"
  >
    <div class="px-4">
      <h1 class="sr-only">
        Mittagsangebote
      </h1>
      <DaySelector
        :selectedDay="selectedDay"
        :disabledNext="!nextDay"
        :disabledPrev="!prevDay"
        @change="onDaySelected"
        class="sm:max-w-sm h-16"
      />
    </div>
    <transition
      mode="out-in"
      leave-active-class="transition-all duration-100 ease-in transform"
      :leave-to-class="`opacity-0 ${isDirectionNext ? '-translate-x-12' : 'translate-x-12'}`"
      enter-active-class="delay-200 transition-all duration-50 ease-out transform"
      :enter-from-class="`opacity-0 ${isDirectionNext ? 'translate-x-1' : '-translate-x-1'}`"
    >
      <!--       <OfferBoxGroup
        v-touch:swipe="onSwipe"
        :key="selectedDayAsISOString"
        class="flex-grow
               pt-4"
      />
-->
      <OfferBoxGroup
        :key="selectedDayAsISOString"
        class="flex-grow
               pt-4"
      />
    </transition>
  </div>
  <ContentError v-else-if="loadingFailed" />
  <ContentLoading v-else />
</template>

<script setup lang="ts">
import ContentError from '@/views/layout/content/ContentError.vue'
import ContentLoading from '@/views/layout/content/ContentLoading.vue'
import DaySelector from '@/views/offers/DaySelector.vue'
import OfferBoxGroup from '@/views/offers/OfferBoxGroup.vue'
import { DaySelectorDirection } from '@/views/offers/dayselector/DaySelectorDirection'
import { formatToISODate } from '@/util/formatting'
import { useLunchStore } from '@/store/lunch'
import { computed, ref } from 'vue'

const {
  isLoading,
  error,
  providers,
  offers,
  selectedLocation,
  selectedDay,
  selectDay,
} = useLunchStore()
const isDirectionNext = ref(true)

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
  const gotoDay = direction === DaySelectorDirection.NEXT ? nextDay.value : prevDay.value
  if (!gotoDay) return

  isDirectionNext.value = direction === DaySelectorDirection.NEXT
  selectDay(gotoDay)
}

function onSwipe(swipeDirection: string) {
  if (swipeDirection === 'left') onDaySelected(DaySelectorDirection.NEXT)
  else if (swipeDirection === 'right') onDaySelected(DaySelectorDirection.PREVIOUS)
}
</script>
