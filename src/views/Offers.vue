<template>
  <OfferBoxGroup v-if="loadingDone" class="offers" />
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
