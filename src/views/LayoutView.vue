<template>
  <div class="layout-view">
    <Header />
    <Content />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { Header, Content } from './layout/'

@Component({
  components: {
    Header,
    Content,
  },
})
export default class LayoutView extends Vue {}
</script>
