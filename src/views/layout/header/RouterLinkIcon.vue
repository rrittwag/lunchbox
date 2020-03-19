<template>
  <router-link :to="to" :exact="exact" v-slot="{ navigate, isActive }">
    <a
      class="router-link flex justify-center items-center
             w-12 h-12"
      :class="isActive ? 'text-neutral-800' : 'text-neutral-300'"
      :href="to"
      :title="title"
      :aria-label="title"
      :aria-current="isActive ? 'true' : undefined"
      @click="navigate"
    >
      <slot />
    </a>
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
.router-link > svg {
  @apply w-8 h-8;
}
</style>
