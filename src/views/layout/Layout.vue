<template>
  <div>
    <Header/>
    <Content/>
  </div>
</template>



<script lang="ts">
import { Component, Vue, Provide } from 'vue-property-decorator'
import { getModule } from 'vuex-module-decorators'
import Header from './header/Header.vue'
import Content from './Content.vue'

// vscode shows decorator errors -> https://github.com/vuejs/vetur/issues/815
@Component({
  components: {
    Header,
    Content,
  },
})
export default class Layout extends Vue {
}
</script>
