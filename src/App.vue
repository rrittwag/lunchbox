<template>
  <div id="app">
    <Layout/>
  </div>
</template>



<script lang="ts">
import { Component, Vue, Provide } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import { Layout } from '@/views/layout'
import { LunchStore } from '@/store'

// vscode shows decorator errors -> https://github.com/vuejs/vetur/issues/815
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



<style lang="scss">
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
}
</style>
