<template>
  <div id="app">
    <LayoutView/>
  </div>
</template>



<script lang="ts">
import { Component, Vue, Provide } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import { LunchStore } from '@/store'
import LayoutView from '@/views/LayoutView.vue'

// vscode shows decorator errors -> https://github.com/vuejs/vetur/issues/815
@Component({
  components: {
    LayoutView,
  },
})
export default class App extends Vue {
  @Provide() lunchStore: LunchStore = getModule(LunchStore)

  created() {
    this.lunchStore.loadFromApi()
  }
}
</script>
