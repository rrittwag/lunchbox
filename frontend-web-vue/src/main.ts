import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import '@/assets/style/index.scss'
import { createPinia } from 'pinia'

// Polyfill für CSS4-Feature focus-visible
import '../node_modules/focus-visible/dist/focus-visible.min.js'
import '@/assets/style/focus-visible.scss'

// Swipe- & Touch-Gesten erkennen
// import Vue2TouchEvents from 'vue2-touch-events'
// Vue.use(Vue2TouchEvents)

// activate PWA
if (typeof window !== 'undefined') {
  import('./pwa')
}

const app = createApp(App)

app.use(router)
app.use(createPinia())

router.isReady().then(() => app.mount('#app'))
