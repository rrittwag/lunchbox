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
        :selectedDay="lunchStore.selectedDay"
        :disabledNext="!nextDay()"
        :disabledPrev="!prevDay()"
        @change="daySelected"
        class="sm:max-w-sm h-16"
      />
    </div>
    <transition
      mode="out-in"
      leave-active-class="transition-all duration-100 ease-in transform"
      :leave-to-class="`opacity-0 ${isDirectionNext ? '-translate-x-12' : 'translate-x-12'}`"
      enter-active-class="delay-200 transition-all duration-50 ease-out transform"
      :enter-class="`opacity-0 ${isDirectionNext ? 'translate-x-1' : '-translate-x-1'}`"
    >
      <OfferBoxGroup
        v-touch:swipe="swiped"
        :key="selectedDayAsISOString"
        class="flex-grow
               pt-4"
      />
    </transition>
  </div>
  <ContentError v-else-if="loadingFailed" />
  <ContentLoading v-else />
</template>

<script lang="ts">
import { Component, Inject, Vue } from 'vue-property-decorator'
import ContentError from '@/views/layout/content/ContentError.vue'
import ContentLoading from '@/views/layout/content/ContentLoading.vue'
import { LunchStore } from '@/store/modules/LunchStore'
import { LoadingState } from '@/store/LoadingState'
import DaySelector from '@/views/offers/DaySelector.vue'
import OfferBoxGroup from '@/views/offers/OfferBoxGroup.vue'
import { DaySelectorDirection } from '@/views/offers/dayselector/DaySelectorDirection'
import { formatToISODate } from '@/util/formatting'

@Component({
  components: {
    DaySelector,
    OfferBoxGroup,
    ContentLoading,
    ContentError,
  },
})
export default class Offers extends Vue {
  @Inject() lunchStore!: LunchStore
  isDirectionNext = true

  get loadingDone(): boolean {
    return this.lunchStore.loadingState === LoadingState.Done
  }

  get loadingFailed(): boolean {
    return this.lunchStore.loadingState === LoadingState.Failed
  }

  get lunchDays(): Date[] {
    const providerIDsForSelectedLocation = this.lunchStore.providers
      .filter(p => p.location === this.lunchStore.selectedLocation.name)
      .map(p => p.id)
    const lunchDays: string[] = this.lunchStore.offers
      .filter(o => providerIDsForSelectedLocation.includes(o.provider))
      .map(o => o.day)
    return Array.from(new Set<string>(lunchDays))
      .map(dayString => new Date(dayString))
      .sort((day1, day2) => day1.getTime() - day2.getTime())
  }

  prevDay(): Date | undefined {
    return this.lunchDays.filter(day => day < this.lunchStore.selectedDay).pop()
  }

  nextDay(): Date | undefined {
    return this.lunchDays.filter(day => day > this.lunchStore.selectedDay)[0]
  }

  get selectedDayAsISOString(): string {
    return formatToISODate(this.lunchStore.selectedDay)
  }

  daySelected(direction: DaySelectorDirection) {
    const gotoDay = direction === DaySelectorDirection.NEXT ? this.nextDay() : this.prevDay()
    if (!gotoDay) return

    this.isDirectionNext = direction === DaySelectorDirection.NEXT
    this.lunchStore.setSelectedDay(gotoDay)
  }

  swiped(swipeDirection: string) {
    if (swipeDirection === 'left') this.daySelected(DaySelectorDirection.NEXT)
    else if (swipeDirection === 'right') this.daySelected(DaySelectorDirection.PREVIOUS)
  }
}
</script>
