<template>
  <div id="app">
    <Layout />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Provide } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import Layout from '@/views/Layout.vue'
import { LunchStore } from '@/store/modules/LunchStore'

@Component({
  components: {
    Layout,
  },
})
export default class App extends Vue {
  @Provide() lunchStore: LunchStore = getModule(LunchStore)

  created() {
    this.lunchStore.loadFromApi()
  }
}
</script>
