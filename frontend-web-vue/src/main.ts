import { createPinia } from 'pinia'
import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import '@/assets/style/index.css'

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
