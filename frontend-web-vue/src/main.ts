import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import '@/assets/style/index.scss'

// register PWA app
// import '@/plugins/registerServiceWorker'

// Polyfill für CSS4-Feature focus-visible
import '../node_modules/focus-visible/dist/focus-visible.min.js'
import '@/assets/style/focus-visible.scss'

// Swipe- & Touch-Gesten erkennen
// import Vue2TouchEvents from 'vue2-touch-events'
// Vue.use(Vue2TouchEvents)

createApp(App).use(router).mount('#app')
