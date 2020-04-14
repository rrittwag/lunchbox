<template>
  <button
    class="text-primary-500 hover:text-primary-600
           disabled:text-primary-300 disabled:cursor-not-allowed"
    :class="{ 'active:text-primary-800': !disabled }"
    :title="title"
    :aria-label="title"
    @click="clicked"
    :disabled="disabled"
    :aria-keyshortcuts="isPrevious ? 'ArrowLeft' : 'ArrowRight'"
  >
    <AngleLeftIcon v-if="isPrevious" class="w-16 h-16" />
    <AngleRightIcon v-else class="w-16 h-16" />
  </button>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import AngleLeftIcon from '@/assets/icons/angle-left.svg'
import AngleRightIcon from '@/assets/icons/angle-right.svg'
import { DaySelectorDirection } from '@/views/offers/dayselector/DaySelectorEvent'

@Component({
  components: {
    AngleLeftIcon,
    AngleRightIcon,
  },
})
export default class DaySelectorButton extends Vue {
  @Prop() disabled!: boolean
  @Prop() direction!: DaySelectorDirection

  clicked() {
    this.$emit('click')
  }

  get isPrevious(): boolean {
    return this.direction === DaySelectorDirection.PREVIOUS
  }

  get title(): string {
    return this.isPrevious ? 'Zu vorherigem Tag wechseln' : 'Zu nächstem Tag wechseln'
  }
}
</script>
