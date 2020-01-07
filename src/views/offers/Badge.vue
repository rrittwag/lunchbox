<template>
  <div
    class="rounded-full
           font-light text-neutral-900
           px-3 py-1 mr-1"
    :class="color"
  >
    {{ label }}
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class Badge extends Vue {
  @Prop() label!: string
  @Prop() color!: string
}
</script>
