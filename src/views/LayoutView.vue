<template>
  <div class="layout-view">
    <Header />
    <Content />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { Header, Content } from './layout/'

// vscode shows decorator errors -> https://github.com/vuejs/vetur/issues/815
@Component({
  components: {
    Header,
    Content,
  },
})
export default class LayoutView extends Vue {}
</script>
