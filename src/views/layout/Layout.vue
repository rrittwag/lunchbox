<template>
  <div>
    <Header/>
    <router-view/>
  </div>
</template>



<script lang="ts">
import { Component, Vue, Provide } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import Header from './header/Header.vue'

// vscode shows decorator errors -> https://github.com/vuejs/vetur/issues/815
@Component({
  components: {
    Header,
  },
})
export default class Layout extends Vue {
}
</script>
