import Vue from 'vue'
import App from './App.vue'
import { router } from '@/router'
import { store } from '@/store'

import '@/assets/style/index.scss'

// register PWA app
import '@/plugins/registerServiceWorker'

// Polyfill für CSS4-Feature focus-visible
import '../node_modules/focus-visible/dist/focus-visible.min.js'
import '@/assets/style/focus-visible.scss'

// Swipe- & Touch-Gesten erkennen
import Vue2TouchEvents from 'vue2-touch-events'
Vue.use(Vue2TouchEvents)

// configuration
Vue.config.productionTip = false

// create root Vue instance
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
