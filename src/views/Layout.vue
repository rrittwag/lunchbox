<template>
  <div class="h-full w-full font-sans antialiased">
    <Header />
    <Content />
  </div>
</template>

<script lang="ts">
import { Component, Provide, Vue, Watch } from 'vue-property-decorator'
import { Theme, ThemeStore } from '@/store/modules/ThemeStore'
import { getModule } from 'vuex-module-decorators'
import Content from '@/views/layout/Content.vue'
import Header from '@/views/layout/Header.vue'

@Component({
  components: {
    Header,
    Content,
  },
})
export default class Layout extends Vue {
  @Provide() themeStore: ThemeStore = getModule(ThemeStore)

  @Watch('themeStore.theme', { immediate: true })
  onThemeChanged(newTheme: Theme, oldTheme?: Theme) {
    // background color works best on <body />, so theme must be set on <body />
    const bodyClasses = window.document.body.classList
    if (oldTheme) bodyClasses.remove(oldTheme.value)
    if (newTheme) bodyClasses.add(newTheme.value)

    // transition on theme change AFTER initial page load
    if (oldTheme && !bodyClasses.contains('bg-transition')) bodyClasses.add('bg-transition')
  }
}
</script>
