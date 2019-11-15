<template>
  <div v-if="loadingDone" class="offers">
    <DaySelector />
    <MessageBox />
    <OfferBoxGroup />
  </div>
  <ContentError v-else-if="loadingFailed" />
  <ContentLoading v-else />
</template>

<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import { LunchStore, LoadingState } from '@/store'
import { DaySelector, MessageBox, OfferBoxGroup } from './offers/'
import { ContentLoading, ContentError } from '@/views/layout/content/index'

@Component({
  components: {
    DaySelector,
    MessageBox,
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
