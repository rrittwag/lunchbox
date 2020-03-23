<template>
  <div class="flex flex-col sm:py-4" v-if="loadingDone">
    <div class="px-4">
      <h1 class="sr-only">
        Mittagsangebote
      </h1>
      <DaySelector class="w-full sm:max-w-sm sm:h-16" />
    </div>
    <OfferBoxGroup class="pt-4" />
  </div>
  <ContentError v-else-if="loadingFailed" />
  <ContentLoading v-else />
</template>

<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import ContentError from '@/views/layout/content/ContentError.vue'
import ContentLoading from '@/views/layout/content/ContentLoading.vue'
import { LunchStore } from '@/store/modules/LunchStore'
import { LoadingState } from '@/store/LoadingState'
import DaySelector from '@/views/offers/DaySelector.vue'
import OfferBoxGroup from '@/views/offers/OfferBoxGroup.vue'

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

  get loadingDone(): boolean {
    return this.lunchStore.loadingState === LoadingState.Done
  }

  get loadingFailed(): boolean {
    return this.lunchStore.loadingState === LoadingState.Failed
  }
}
</script>
