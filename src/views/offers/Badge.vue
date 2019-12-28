<template>
  <div
    class="rounded-full
            bg-yellow-200 text-gray-900 font-light
            px-3 py-1 mr-1"
  >
    {{ label }}
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class Badge extends Vue {
  @Prop() label!: string
}
</script>
