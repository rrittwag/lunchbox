import Vue from 'vue'
import App from './App.vue'
import { router } from '@/router'
import { store } from '@/store'

// Polyfill für CSS4-Feature focus-visible
import '../node_modules/focus-visible/dist/focus-visible.min.js'

// register PWA app
import '@/plugins/registerServiceWorker'

// Tailwind CSS styles
import '@/assets/tailwind.scss'

// configuration
Vue.config.productionTip = false

// create root Vue instance
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
