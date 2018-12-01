<template>
  <div id="app">
    <Header/>
    <router-view/>
  </div>
</template>



<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import Header from '@/components/Header.vue'
import LunchStore from '@/store/lunch'

// vscode shows decorator errors -> https://github.com/vuejs/vetur/issues/815
@Component({
  components: {
    Header,
  },
})
export default class App extends Vue {
  private lunchStore: LunchStore = getModule(LunchStore)

  created() {
    this.lunchStore.fetchFromApi()
  }
}
</script>



<style lang="scss">
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
}
</style>
