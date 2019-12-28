<template>
  <div>TODO</div>
</template>

<script lang="ts">
import { Component, Vue, Inject } from 'vue-property-decorator'
import { LunchStore } from '@/store'

@Component
export default class LocationSelector extends Vue {
  @Inject() lunchStore!: LunchStore
}
</script>
