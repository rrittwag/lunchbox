<template>
  <router-link :to="to" exact v-slot="{ navigate, isActive }">
    <button
      class="router-link-button flex justify-center items-center
             w-12 h-12
             mx-2"
      :class="isActive ? 'text-neutral-300' : 'text-neutral-900'"
      @click="navigate"
      :title="title"
      :aria-label="title"
    >
      <slot />
    </button>
  </router-link>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class RouterLinkIcon extends Vue {
  @Prop() to!: string
  @Prop({ type: Boolean }) exact!: boolean
  @Prop() title!: string
}
</script>

<style lang="scss">
.router-link-button > svg {
  @apply w-8 h-8;
}
</style>
